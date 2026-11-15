package com.lowkeyarhan.ecommerce.cart.repository;

import com.lowkeyarhan.ecommerce.cart.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);
    long countByCart_Id(Long cartId);
}
