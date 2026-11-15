-- MySQL 8 translation of the SGA1 Project 4 schema.
-- The domain tables and integrity rules are preserved; portability change is documented in README/report.

CREATE TABLE customers (
    customer_id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    email VARCHAR(254) NOT NULL,
    phone VARCHAR(30),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_customers PRIMARY KEY (customer_id),
    CONSTRAINT uq_customers_email UNIQUE (email),
    CONSTRAINT ck_customers_email_shape CHECK (LOCATE('@', email) > 1)
) ENGINE=InnoDB;

CREATE TABLE customer_addresses (
    address_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    address_line1 VARCHAR(120) NOT NULL,
    address_line2 VARCHAR(120),
    city VARCHAR(80) NOT NULL,
    state_region VARCHAR(80) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(80) NOT NULL DEFAULT 'India',
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_customer_addresses PRIMARY KEY (address_id),
    CONSTRAINT uq_address_customer_pair UNIQUE (customer_id, address_id),
    CONSTRAINT fk_address_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE sellers (
    seller_id BIGINT NOT NULL AUTO_INCREMENT,
    seller_name VARCHAR(120) NOT NULL,
    contact_email VARCHAR(254) NOT NULL,
    status VARCHAR(12) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_sellers PRIMARY KEY (seller_id),
    CONSTRAINT uq_sellers_name UNIQUE (seller_name),
    CONSTRAINT uq_sellers_email UNIQUE (contact_email),
    CONSTRAINT ck_seller_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
) ENGINE=InnoDB;

CREATE TABLE categories (
    category_id BIGINT NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(80) NOT NULL,
    parent_category_id BIGINT,
    CONSTRAINT pk_categories PRIMARY KEY (category_id),
    CONSTRAINT uq_category_name UNIQUE (category_name),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    CONSTRAINT ck_category_not_own_parent CHECK (parent_category_id IS NULL OR parent_category_id <> category_id)
) ENGINE=InnoDB;

CREATE TABLE products (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    seller_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    sku VARCHAR(40) NOT NULL,
    product_name VARCHAR(140) NOT NULL,
    description TEXT,
    unit_price DECIMAL(12,2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 5,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_products PRIMARY KEY (product_id),
    CONSTRAINT uq_products_sku UNIQUE (sku),
    CONSTRAINT fk_product_seller FOREIGN KEY (seller_id) REFERENCES sellers(seller_id) ON DELETE RESTRICT,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    CONSTRAINT ck_product_price CHECK (unit_price > 0),
    CONSTRAINT ck_product_stock CHECK (stock_quantity >= 0),
    CONSTRAINT ck_product_reorder CHECK (reorder_level >= 0),
    INDEX idx_products_category_active (category_id, is_active),
    INDEX idx_products_seller (seller_id),
    INDEX idx_products_stock (stock_quantity, reorder_level)
) ENGINE=InnoDB;

CREATE TABLE carts (
    cart_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    status VARCHAR(12) NOT NULL DEFAULT 'OPEN',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    -- MySQL has no partial unique indexes. NULL for non-open carts permits history while uniqueness
    -- for an OPEN cart/customer pair is enforced by the generated column.
    open_customer_id BIGINT GENERATED ALWAYS AS (CASE WHEN status = 'OPEN' THEN customer_id ELSE NULL END) STORED,
    CONSTRAINT pk_carts PRIMARY KEY (cart_id),
    CONSTRAINT fk_cart_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT,
    CONSTRAINT uq_one_open_cart_per_customer UNIQUE (open_customer_id),
    CONSTRAINT ck_cart_status CHECK (status IN ('OPEN', 'CONVERTED', 'ABANDONED')),
    INDEX idx_carts_customer_status (customer_id, status)
) ENGINE=InnoDB;

CREATE TABLE cart_items (
    cart_item_id BIGINT NOT NULL AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    added_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_cart_items PRIMARY KEY (cart_item_id),
    CONSTRAINT uq_cart_product UNIQUE (cart_id, product_id),
    CONSTRAINT fk_cart_item_cart FOREIGN KEY (cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    CONSTRAINT ck_cart_item_quantity CHECK (quantity > 0),
    INDEX idx_cart_items_product (product_id)
) ENGINE=InnoDB;

CREATE TABLE orders (
    order_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    shipping_address_id BIGINT NOT NULL,
    order_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    status VARCHAR(14) NOT NULL DEFAULT 'CONFIRMED',
    subtotal DECIMAL(12,2) NOT NULL DEFAULT 0,
    shipping_fee DECIMAL(12,2) NOT NULL DEFAULT 0,
    tax_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(12,2) GENERATED ALWAYS AS (subtotal + shipping_fee + tax_amount) STORED,
    CONSTRAINT pk_orders PRIMARY KEY (order_id),
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT,
    CONSTRAINT fk_orders_customer_address FOREIGN KEY (customer_id, shipping_address_id)
        REFERENCES customer_addresses(customer_id, address_id) ON DELETE RESTRICT,
    CONSTRAINT ck_order_status CHECK (status IN ('CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'RETURNED')),
    CONSTRAINT ck_order_subtotal CHECK (subtotal >= 0),
    CONSTRAINT ck_order_shipping CHECK (shipping_fee >= 0),
    CONSTRAINT ck_order_tax CHECK (tax_amount >= 0),
    INDEX idx_orders_customer_date (customer_id, order_date),
    INDEX idx_orders_status_date (status, order_date)
) ENGINE=InnoDB;

CREATE TABLE order_items (
    order_item_id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(12,2) NOT NULL,
    line_total DECIMAL(14,2) GENERATED ALWAYS AS (unit_price * quantity) STORED,
    CONSTRAINT pk_order_items PRIMARY KEY (order_item_id),
    CONSTRAINT uq_order_product UNIQUE (order_id, product_id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE RESTRICT,
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    CONSTRAINT ck_order_item_quantity CHECK (quantity > 0),
    CONSTRAINT ck_order_item_unit_price CHECK (unit_price > 0),
    INDEX idx_order_items_product (product_id)
) ENGINE=InnoDB;

CREATE TABLE payments (
    payment_id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    payment_kind VARCHAR(8) NOT NULL,
    method VARCHAR(16) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    related_payment_id BIGINT,
    reference_code VARCHAR(100),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_payments PRIMARY KEY (payment_id),
    CONSTRAINT uq_payment_reference UNIQUE (reference_code),
    CONSTRAINT uq_payment_order_pair UNIQUE (payment_id, order_id),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE RESTRICT,
    CONSTRAINT fk_refund_same_order FOREIGN KEY (related_payment_id, order_id)
        REFERENCES payments(payment_id, order_id) ON DELETE RESTRICT,
    CONSTRAINT ck_payment_kind CHECK (payment_kind IN ('CHARGE', 'REFUND')),
    CONSTRAINT ck_payment_method CHECK (method IN ('CARD', 'UPI', 'NET_BANKING', 'COD', 'WALLET')),
    CONSTRAINT ck_payment_amount CHECK (amount > 0),
    CONSTRAINT ck_payment_status CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),
    CONSTRAINT ck_payment_reference_kind CHECK ((payment_kind = 'CHARGE' AND related_payment_id IS NULL) OR (payment_kind = 'REFUND' AND related_payment_id IS NOT NULL)),
    INDEX idx_payments_order (order_id, payment_kind, status)
) ENGINE=InnoDB;

CREATE TABLE shipments (
    shipment_id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    carrier VARCHAR(80) NOT NULL,
    tracking_number VARCHAR(100) NOT NULL,
    status VARCHAR(12) NOT NULL DEFAULT 'PREPARING',
    shipped_at DATETIME(6),
    delivered_at DATETIME(6),
    CONSTRAINT pk_shipments PRIMARY KEY (shipment_id),
    CONSTRAINT uq_shipment_order UNIQUE (order_id),
    CONSTRAINT uq_shipment_tracking UNIQUE (tracking_number),
    CONSTRAINT fk_shipment_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE RESTRICT,
    CONSTRAINT ck_shipment_status CHECK (status IN ('PREPARING', 'SHIPPED', 'IN_TRANSIT', 'DELIVERED', 'FAILED')),
    CONSTRAINT ck_shipment_time_order CHECK (delivered_at IS NULL OR shipped_at IS NULL OR delivered_at >= shipped_at)
) ENGINE=InnoDB;

CREATE TABLE product_returns (
    return_id BIGINT NOT NULL AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    requested_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    reason VARCHAR(300) NOT NULL,
    status VARCHAR(12) NOT NULL DEFAULT 'REQUESTED',
    processed_at DATETIME(6),
    CONSTRAINT pk_product_returns PRIMARY KEY (return_id),
    CONSTRAINT fk_return_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE RESTRICT,
    CONSTRAINT ck_return_status CHECK (status IN ('REQUESTED', 'APPROVED', 'REJECTED', 'RECEIVED')),
    INDEX idx_returns_status_date (status, requested_at)
) ENGINE=InnoDB;

CREATE TABLE return_items (
    return_item_id BIGINT NOT NULL AUTO_INCREMENT,
    return_id BIGINT NOT NULL,
    order_item_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT pk_return_items PRIMARY KEY (return_item_id),
    CONSTRAINT uq_return_order_item UNIQUE (return_id, order_item_id),
    CONSTRAINT fk_return_item_return FOREIGN KEY (return_id) REFERENCES product_returns(return_id) ON DELETE RESTRICT,
    CONSTRAINT fk_return_item_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(order_item_id) ON DELETE RESTRICT,
    CONSTRAINT ck_return_item_quantity CHECK (quantity > 0)
) ENGINE=InnoDB;

CREATE TABLE product_reviews (
    review_id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    rating SMALLINT NOT NULL,
    review_text VARCHAR(800),
    reviewed_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_product_reviews PRIMARY KEY (review_id),
    CONSTRAINT uq_customer_product_review UNIQUE (customer_id, product_id),
    CONSTRAINT fk_review_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT,
    CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    CONSTRAINT ck_review_rating CHECK (rating BETWEEN 1 AND 5),
    INDEX idx_reviews_product (product_id)
) ENGINE=InnoDB;

CREATE TABLE inventory_movements (
    movement_id BIGINT NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    order_id BIGINT,
    quantity_delta INT NOT NULL,
    reason VARCHAR(12) NOT NULL,
    note VARCHAR(300),
    occurred_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_inventory_movements PRIMARY KEY (movement_id),
    CONSTRAINT fk_movement_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    CONSTRAINT fk_movement_order FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE RESTRICT,
    CONSTRAINT ck_movement_nonzero CHECK (quantity_delta <> 0),
    CONSTRAINT ck_inventory_reason_direction CHECK (
        (reason = 'SALE' AND quantity_delta < 0 AND order_id IS NOT NULL) OR
        (reason = 'RETURN' AND quantity_delta > 0 AND order_id IS NOT NULL) OR
        (reason = 'RESTOCK' AND quantity_delta > 0) OR reason = 'ADJUSTMENT'
    ),
    INDEX idx_movements_product_date (product_id, occurred_at),
    INDEX idx_movements_order (order_id)
) ENGINE=InnoDB;
