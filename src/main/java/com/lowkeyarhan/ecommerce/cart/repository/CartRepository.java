package com.lowkeyarhan.ecommerce.cart.repository;

import com.lowkeyarhan.ecommerce.cart.model.Cart;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths={"items", "items.product", "items.product.seller", "items.product.category"})
    Optional<Cart> findFirstByCustomer_IdAndStatusOrderByCreatedAtDesc(Long customerId, String status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select distinct c from Cart c left join fetch c.items ci left join fetch ci.product where c.customer.id = :customerId and c.status = 'OPEN'")
    Optional<Cart> lockOpenCart(@Param("customerId") Long customerId);
    long countByStatus(String status);
}
