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

@Entity @Table(name="categories")
@Getter @Setter @NoArgsConstructor
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="category_id") private Long id;
    @Column(name="category_name", nullable=false, unique=true, length=80) private String categoryName;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="parent_category_id") @ToString.Exclude @EqualsAndHashCode.Exclude private Category parentCategory;
    @OneToMany(mappedBy="parentCategory") @ToString.Exclude @EqualsAndHashCode.Exclude private List<Category> children = new ArrayList<>();
    @OneToMany(mappedBy="category") @ToString.Exclude @EqualsAndHashCode.Exclude private List<Product> products = new ArrayList<>();
}
