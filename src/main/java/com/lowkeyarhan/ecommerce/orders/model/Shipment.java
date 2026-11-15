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

@Entity @Table(name="shipments")
@Getter @Setter @NoArgsConstructor
public class Shipment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="shipment_id") private Long id;
    @OneToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="order_id", nullable=false, unique=true) @ToString.Exclude @EqualsAndHashCode.Exclude private PurchaseOrder order;
    @Column(nullable=false, length=80) private String carrier;
    @Column(name="tracking_number", nullable=false, unique=true, length=100) private String trackingNumber;
    @Column(nullable=false, length=12) private String status = "PREPARING";
    @Column(name="shipped_at") private LocalDateTime shippedAt;
    @Column(name="delivered_at") private LocalDateTime deliveredAt;
}
