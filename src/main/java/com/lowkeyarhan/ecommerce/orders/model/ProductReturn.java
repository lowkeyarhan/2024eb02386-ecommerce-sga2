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

@Entity @Table(name="product_returns")
@Getter @Setter @NoArgsConstructor
public class ProductReturn {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="return_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="order_id", nullable=false) @ToString.Exclude @EqualsAndHashCode.Exclude private PurchaseOrder order;
    @Column(name="requested_at", nullable=false, updatable=false) private LocalDateTime requestedAt;
    @Column(nullable=false, length=300) private String reason;
    @Column(nullable=false, length=12) private String status = "REQUESTED";
    @Column(name="processed_at") private LocalDateTime processedAt;
    @OneToMany(mappedBy="productReturn", cascade=CascadeType.ALL, orphanRemoval=true) @ToString.Exclude @EqualsAndHashCode.Exclude private List<ReturnItem> items = new ArrayList<>();
    @PrePersist void prePersist() { if (requestedAt == null) requestedAt = LocalDateTime.now(); }
}
