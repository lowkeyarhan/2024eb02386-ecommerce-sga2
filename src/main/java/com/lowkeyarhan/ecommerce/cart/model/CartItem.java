package com.lowkeyarhan.ecommerce.cart.model;

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
import com.lowkeyarhan.ecommerce.catalog.model.Product;

@Entity @Table(name="cart_items", uniqueConstraints=@UniqueConstraint(name="uq_cart_product", columnNames={"cart_id","product_id"}))
@Getter @Setter @NoArgsConstructor
public class CartItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="cart_item_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="cart_id", nullable=false) @ToString.Exclude @EqualsAndHashCode.Exclude private Cart cart;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="product_id", nullable=false) private Product product;
    @Column(nullable=false) private Integer quantity;
    @Column(name="added_at", nullable=false, updatable=false) private LocalDateTime addedAt;
    @PrePersist void prePersist() { if (addedAt == null) addedAt = LocalDateTime.now(); }
}
