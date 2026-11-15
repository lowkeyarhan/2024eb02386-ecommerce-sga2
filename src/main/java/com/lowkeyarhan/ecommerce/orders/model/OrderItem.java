package com.lowkeyarhan.ecommerce.orders.model;

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

@Entity @Table(name="order_items", uniqueConstraints=@UniqueConstraint(name="uq_order_product", columnNames={"order_id","product_id"}))
@Getter @Setter @NoArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="order_item_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="order_id", nullable=false) @ToString.Exclude @EqualsAndHashCode.Exclude private PurchaseOrder order;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="product_id", nullable=false) private Product product;
    @Column(nullable=false) private Integer quantity;
    @Column(name="unit_price", nullable=false, precision=12, scale=2) private BigDecimal unitPrice;
    @Column(name="line_total", insertable=false, updatable=false, precision=14, scale=2) private BigDecimal lineTotal;
}
