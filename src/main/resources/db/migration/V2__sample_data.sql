INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES
(1, 'Arhan', 'Demo', 'arhan.demo@example.test', '+91-9000000001'),
(2, 'Maya', 'Sen', 'maya.sen@example.test', '+91-9000000002'),
(3, 'Rohan', 'Das', 'rohan.das@example.test', '+91-9000000003');

INSERT INTO customer_addresses (address_id, customer_id, address_line1, address_line2, city, state_region, postal_code, country, is_default) VALUES
(1, 1, '12 Lake View Road', 'Flat 3B', 'Bengaluru', 'Karnataka', '560001', 'India', TRUE),
(2, 2, '44 Park Street', NULL, 'Kolkata', 'West Bengal', '700016', 'India', TRUE),
(3, 3, '8 College Road', NULL, 'Kharagpur', 'West Bengal', '721302', 'India', TRUE);

INSERT INTO sellers (seller_id, seller_name, contact_email, status) VALUES
(1, 'Northstar Electronics', 'sales@northstar.example.test', 'ACTIVE'),
(2, 'Everyday Essentials', 'orders@everyday.example.test', 'ACTIVE'),
(3, 'Studio Supply Co.', 'hello@studiosupply.example.test', 'ACTIVE');

INSERT INTO categories (category_id, category_name, parent_category_id) VALUES
(1, 'Electronics', NULL),
(2, 'Computers', 1),
(3, 'Accessories', 1),
(4, 'Home & Office', NULL),
(5, 'Audio', 1);

INSERT INTO products (product_id, seller_id, category_id, sku, product_name, description, unit_price, stock_quantity, reorder_level, is_active) VALUES
(1, 1, 2, 'LAP-001', 'Everyday Work Laptop', '14-inch productivity laptop for study and work.', 58990.00, 19, 4, TRUE),
(2, 1, 3, 'HUB-001', 'USB-C 7-in-1 Hub', 'Compact USB-C hub with HDMI, USB-A and card reader.', 2499.00, 4, 6, TRUE),
(3, 2, 4, 'DSK-001', 'Minimal Desk Lamp', 'Dimmable LED desk lamp.', 1799.00, 28, 5, TRUE),
(4, 3, 5, 'AUD-001', 'Studio Headphones', 'Closed-back headphones for editing and monitoring.', 6999.00, 8, 3, TRUE),
(5, 1, 3, 'MSE-001', 'Wireless Mouse', 'Ergonomic wireless mouse.', 1299.00, 42, 8, TRUE),
(6, 2, 4, 'CHR-001', 'Ergonomic Chair', 'Adjustable task chair with lumbar support.', 8499.00, 6, 4, TRUE),
(7, 3, 5, 'MIC-001', 'USB Condenser Microphone', 'USB microphone for calls and recording.', 5499.00, 3, 5, TRUE),
(8, 1, 3, 'KEY-001', 'Mechanical Keyboard', 'Compact mechanical keyboard.', 3799.00, 17, 5, TRUE),
(9, 2, 4, 'NOTE-001', 'Hardcover Notebook', 'A5 ruled notebook.', 249.00, 90, 20, TRUE),
(10, 3, 5, 'CAB-001', 'USB-C Cable', 'Braided 1 metre USB-C cable.', 399.00, 2, 10, TRUE);

-- One prior order provides real dashboard/history/report data for the demo.
INSERT INTO orders (order_id, customer_id, shipping_address_id, order_date, status, subtotal, shipping_fee, tax_amount)
VALUES (1, 1, 1, DATE_SUB(CURRENT_TIMESTAMP(6), INTERVAL 4 DAY), 'DELIVERED', 3798.00, 49.00, 683.64);

INSERT INTO order_items (order_item_id, order_id, product_id, quantity, unit_price) VALUES
(1, 1, 2, 1, 2499.00),
(2, 1, 5, 1, 1299.00);

INSERT INTO payments (payment_id, order_id, payment_kind, method, amount, status, related_payment_id, reference_code)
VALUES (1, 1, 'CHARGE', 'UPI', 4530.64, 'COMPLETED', NULL, 'DEMO-CHARGE-0001');

INSERT INTO shipments (shipment_id, order_id, carrier, tracking_number, status, shipped_at, delivered_at)
VALUES (1, 1, 'Demo Express', 'DEMO-TRACK-0001', 'DELIVERED', DATE_SUB(CURRENT_TIMESTAMP(6), INTERVAL 3 DAY), DATE_SUB(CURRENT_TIMESTAMP(6), INTERVAL 1 DAY));

INSERT INTO inventory_movements (product_id, order_id, quantity_delta, reason, note) VALUES
(2, 1, -1, 'SALE', 'Seed order stock movement'),
(5, 1, -1, 'SALE', 'Seed order stock movement'),
(2, NULL, 6, 'RESTOCK', 'Opening stock ledger entry'),
(7, NULL, 3, 'RESTOCK', 'Opening stock ledger entry'),
(10, NULL, 2, 'RESTOCK', 'Opening stock ledger entry');

INSERT INTO product_reviews (customer_id, product_id, rating, review_text)
VALUES (1, 5, 5, 'Reliable and comfortable for long work sessions.');

INSERT INTO carts (cart_id, customer_id, status) VALUES (1, 1, 'OPEN');
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (1, 3, 1), (1, 8, 1);
