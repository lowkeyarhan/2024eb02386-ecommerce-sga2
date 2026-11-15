package com.lowkeyarhan.ecommerce.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartPageView(Long cartId, Long customerId, List<CartItemView> items, BigDecimal subtotal, int itemCount) {}
