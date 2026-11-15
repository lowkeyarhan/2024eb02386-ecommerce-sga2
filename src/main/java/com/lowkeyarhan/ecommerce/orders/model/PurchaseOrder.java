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
import com.lowkeyarhan.ecommerce.customer.model.Customer;
import com.lowkeyarhan.ecommerce.customer.model.CustomerAddress;

@Entity @Table(name="orders")
@Getter @Setter @NoArgsConstructor
public class PurchaseOrder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="order_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="customer_id", nullable=false) private Customer customer;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumns({@JoinColumn(name="customer_id", referencedColumnName="customer_id", insertable=false, updatable=false), @JoinColumn(name="shipping_address_id", referencedColumnName="address_id", nullable=false)}) private CustomerAddress shippingAddress;
    @Column(name="order_date", nullable=false, updatable=false) private LocalDateTime orderDate;
    @Column(nullable=false, length=14) private String status = "CONFIRMED";
    @Column(nullable=false, precision=12, scale=2) private BigDecimal subtotal = BigDecimal.ZERO;
    @Column(name="shipping_fee", nullable=false, precision=12, scale=2) private BigDecimal shippingFee = BigDecimal.ZERO;
    @Column(name="tax_amount", nullable=false, precision=12, scale=2) private BigDecimal taxAmount = BigDecimal.ZERO;
    @Column(name="total_amount", insertable=false, updatable=false, precision=12, scale=2) private BigDecimal totalAmount;
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true) @ToString.Exclude @EqualsAndHashCode.Exclude private List<OrderItem> items = new ArrayList<>();
    @OneToMany(mappedBy="order") @ToString.Exclude @EqualsAndHashCode.Exclude private List<Payment> payments = new ArrayList<>();
    @OneToOne(mappedBy="order", cascade=CascadeType.ALL) @ToString.Exclude @EqualsAndHashCode.Exclude private Shipment shipment;
    @OneToMany(mappedBy="order") @ToString.Exclude @EqualsAndHashCode.Exclude private List<ProductReturn> returns = new ArrayList<>();
    @PrePersist void prePersist() { if (orderDate == null) orderDate = LocalDateTime.now(); }
}
