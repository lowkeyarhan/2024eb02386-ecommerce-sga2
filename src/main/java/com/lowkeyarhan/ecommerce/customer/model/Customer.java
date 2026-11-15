package com.lowkeyarhan.ecommerce.customer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.lowkeyarhan.ecommerce.cart.model.Cart;
import com.lowkeyarhan.ecommerce.orders.model.PurchaseOrder;

@Entity @Table(name="customers")
@Getter @Setter @NoArgsConstructor
public class Customer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="customer_id") private Long id;
    @Column(name="first_name", nullable=false, length=60) private String firstName;
    @Column(name="last_name", nullable=false, length=60) private String lastName;
    @Column(nullable=false, unique=true, length=254) private String email;
    @Column(length=30) private String phone;
    @Column(name="created_at", nullable=false, updatable=false) private LocalDateTime createdAt;
    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL, orphanRemoval=true) @ToString.Exclude @EqualsAndHashCode.Exclude private List<CustomerAddress> addresses = new ArrayList<>();
    @OneToMany(mappedBy="customer") @ToString.Exclude @EqualsAndHashCode.Exclude private List<Cart> carts = new ArrayList<>();
    @OneToMany(mappedBy="customer") @ToString.Exclude @EqualsAndHashCode.Exclude private List<PurchaseOrder> orders = new ArrayList<>();
    @PrePersist void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
}
