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

@Entity @Table(name="return_items", uniqueConstraints=@UniqueConstraint(name="uq_return_order_item", columnNames={"return_id","order_item_id"}))
@Getter @Setter @NoArgsConstructor
public class ReturnItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="return_item_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="return_id", nullable=false) @ToString.Exclude @EqualsAndHashCode.Exclude private ProductReturn productReturn;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="order_item_id", nullable=false) private OrderItem orderItem;
    @Column(nullable=false) private Integer quantity;
}
