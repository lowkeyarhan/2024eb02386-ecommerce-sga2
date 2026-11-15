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

@Entity @Table(name="payments", uniqueConstraints=@UniqueConstraint(name="uq_payment_order_pair", columnNames={"payment_id","order_id"}))
@Getter @Setter @NoArgsConstructor
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="payment_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="order_id", nullable=false) @ToString.Exclude @EqualsAndHashCode.Exclude private PurchaseOrder order;
    @Column(name="payment_kind", nullable=false, length=8) private String paymentKind;
    @Column(nullable=false, length=16) private String method;
    @Column(nullable=false, precision=12, scale=2) private BigDecimal amount;
    @Column(nullable=false, length=10) private String status = "PENDING";
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumns({@JoinColumn(name="related_payment_id", referencedColumnName="payment_id"), @JoinColumn(name="order_id", referencedColumnName="order_id", insertable=false, updatable=false)}) private Payment relatedPayment;
    @Column(name="reference_code", unique=true, length=100) private String referenceCode;
    @Column(name="created_at", nullable=false, updatable=false) private LocalDateTime createdAt;
    @PrePersist void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
}
