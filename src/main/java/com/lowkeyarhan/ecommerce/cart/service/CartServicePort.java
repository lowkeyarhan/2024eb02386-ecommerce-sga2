package com.lowkeyarhan.ecommerce.cart.service;

import com.lowkeyarhan.ecommerce.cart.dto.CartPageView;
import com.lowkeyarhan.ecommerce.cart.model.Cart;
import java.util.Optional;

public interface CartServicePort { Cart getOrCreateOpenCart(Long customerId); Cart add(Long customerId, Long productId, Integer quantity); Cart update(Long customerId, Long itemId, Integer quantity); Cart remove(Long customerId, Long itemId); Optional<Cart> findOpenCart(Long customerId); CartPageView view(Long customerId); }
