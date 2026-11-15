package com.lowkeyarhan.ecommerce.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CheckoutForm {
    @NotNull private Long shippingAddressId;
}
