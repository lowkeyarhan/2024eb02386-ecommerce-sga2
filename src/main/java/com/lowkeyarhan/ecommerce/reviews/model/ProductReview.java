package com.lowkeyarhan.ecommerce.reviews.model;

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
import com.lowkeyarhan.ecommerce.catalog.model.Product;

@Entity @Table(name="product_reviews", uniqueConstraints=@UniqueConstraint(name="uq_customer_product_review", columnNames={"customer_id","product_id"}))
@Getter @Setter @NoArgsConstructor
public class ProductReview {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="review_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="customer_id", nullable=false) private Customer customer;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="product_id", nullable=false) private Product product;
    @Column(nullable=false) private Short rating;
    @Column(name="review_text", length=800) private String reviewText;
    @Column(name="reviewed_at", nullable=false, updatable=false) private LocalDateTime reviewedAt;
    @PrePersist void prePersist() { if (reviewedAt == null) reviewedAt = LocalDateTime.now(); }
}
