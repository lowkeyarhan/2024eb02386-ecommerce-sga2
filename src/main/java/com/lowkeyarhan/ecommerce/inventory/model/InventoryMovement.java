package com.lowkeyarhan.ecommerce.inventory.model;

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
import com.lowkeyarhan.ecommerce.orders.model.PurchaseOrder;

@Entity @Table(name="inventory_movements")
@Getter @Setter @NoArgsConstructor
public class InventoryMovement {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="movement_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="product_id", nullable=false) private Product product;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id") private PurchaseOrder order;
    @Column(name="quantity_delta", nullable=false) private Integer quantityDelta;
    @Column(nullable=false, length=12) private String reason;
    @Column(length=300) private String note;
    @Column(name="occurred_at", nullable=false, updatable=false) private LocalDateTime occurredAt;
    @PrePersist void prePersist() { if (occurredAt == null) occurredAt = LocalDateTime.now(); }
}
