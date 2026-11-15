package com.lowkeyarhan.ecommerce.catalog.model;

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

@Entity @Table(name="sellers")
@Getter @Setter @NoArgsConstructor
public class Seller {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="seller_id") private Long id;
    @Column(name="seller_name", nullable=false, unique=true, length=120) private String sellerName;
    @Column(name="contact_email", nullable=false, unique=true, length=254) private String contactEmail;
    @Column(nullable=false, length=12) private String status = "ACTIVE";
    @Column(name="created_at", nullable=false, updatable=false) private LocalDateTime createdAt;
    @OneToMany(mappedBy="seller") @ToString.Exclude @EqualsAndHashCode.Exclude private List<Product> products = new ArrayList<>();
    @PrePersist void prePersist() { if (createdAt == null) createdAt = LocalDateTime.now(); }
}
