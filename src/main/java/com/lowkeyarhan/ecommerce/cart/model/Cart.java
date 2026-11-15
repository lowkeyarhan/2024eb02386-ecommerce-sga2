package com.lowkeyarhan.ecommerce.cart.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.lowkeyarhan.ecommerce.customer.model.Customer;

@Entity @Table(name="carts")
@Getter @Setter @NoArgsConstructor
public class Cart {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="cart_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="customer_id", nullable=false) private Customer customer;
    @Column(nullable=false, length=12) private String status = "OPEN";
    @Column(name="created_at", nullable=false, updatable=false) private LocalDateTime createdAt;
    @OneToMany(mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval=true) @ToString.Exclude @EqualsAndHashCode.Exclude private List<CartItem> items = new ArrayList<>();
    @PrePersist void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
}
