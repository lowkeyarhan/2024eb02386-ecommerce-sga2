package com.lowkeyarhan.ecommerce.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReturnRequestForm {
    @NotNull private Long orderId;
    @NotNull private Long orderItemId;
    @NotNull @Min(1) private Integer quantity;
    @NotBlank @Size(max=300) private String reason;
}
