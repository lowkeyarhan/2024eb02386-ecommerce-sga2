package com.lowkeyarhan.ecommerce.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RestockForm {
    @NotNull private Long productId;
    @NotNull @Min(1) private Integer quantity;
    @Size(max=300) private String note;
}
