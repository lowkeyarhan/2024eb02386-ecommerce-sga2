package com.lowkeyarhan.ecommerce.cart.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class CartItemView {
    private Long id;
    private Long productId;
    private String sku;
    private String productName;
    private Integer quantity;
    private Integer stockQuantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
