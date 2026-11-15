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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.lowkeyarhan.ecommerce.orders.model.PurchaseOrder;

@Entity @Table(name="customer_addresses", uniqueConstraints=@UniqueConstraint(name="uq_address_customer_pair", columnNames={"customer_id","address_id"}))
@Getter @Setter @NoArgsConstructor
public class CustomerAddress {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="address_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="customer_id", nullable=false) @ToString.Exclude @EqualsAndHashCode.Exclude private Customer customer;
    @Column(name="address_line1", nullable=false, length=120) private String addressLine1;
    @Column(name="address_line2", length=120) private String addressLine2;
    @Column(nullable=false, length=80) private String city;
    @Column(name="state_region", nullable=false, length=80) private String stateRegion;
    @Column(name="postal_code", nullable=false, length=20) private String postalCode;
    @Column(nullable=false, length=80) private String country = "India";
    @Column(name="is_default", nullable=false) private boolean defaultAddress;
}
