package com.lowkeyarhan.ecommerce.reviews.repository;

import com.lowkeyarhan.ecommerce.reviews.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    boolean existsByCustomer_IdAndProduct_Id(Long customerId, Long productId);
}
