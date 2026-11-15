package com.lowkeyarhan.ecommerce.catalog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.lowkeyarhan.ecommerce.cart.model.Cart;
import com.lowkeyarhan.ecommerce.cart.model.CartItem;
import com.lowkeyarhan.ecommerce.orders.model.OrderItem;
import com.lowkeyarhan.ecommerce.reviews.model.ProductReview;
import com.lowkeyarhan.ecommerce.inventory.model.InventoryMovement;

@Entity @Table(name="products")
@Getter @Setter @NoArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="product_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="seller_id", nullable=false) private Seller seller;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="category_id", nullable=false) private Category category;
    @Column(nullable=false, unique=true, length=40) private String sku;
    @Column(name="product_name", nullable=false, length=140) private String productName;
    @Column(columnDefinition="text") private String description;
    @Column(name="unit_price", nullable=false, precision=12, scale=2) private BigDecimal unitPrice;
    @Column(name="stock_quantity", nullable=false) private Integer stockQuantity = 0;
    @Column(name="reorder_level", nullable=false) private Integer reorderLevel = 5;
    @Column(name="is_active", nullable=false) private boolean active = true;
    @Column(name="created_at", nullable=false, updatable=false) private LocalDateTime createdAt;
    @OneToMany(mappedBy="product") @ToString.Exclude @EqualsAndHashCode.Exclude private List<CartItem> cartItems = new ArrayList<>();
    @OneToMany(mappedBy="product") @ToString.Exclude @EqualsAndHashCode.Exclude private List<OrderItem> orderItems = new ArrayList<>();
    @OneToMany(mappedBy="product") @ToString.Exclude @EqualsAndHashCode.Exclude private List<ProductReview> reviews = new ArrayList<>();
    @OneToMany(mappedBy="product") @ToString.Exclude @EqualsAndHashCode.Exclude private List<InventoryMovement> inventoryMovements = new ArrayList<>();
    @PrePersist void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
}
