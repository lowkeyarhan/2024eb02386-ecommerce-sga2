package com.lowkeyarhan.ecommerce.catalog.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductForm {
    private Long id;
    @NotBlank @Size(max=40) private String sku;
    @NotBlank @Size(max=140) private String productName;
    @Size(max=4000) private String description;
    @NotNull @DecimalMin(value="0.01") private BigDecimal unitPrice;
    @NotNull @Min(0) private Integer stockQuantity = 0;
    @NotNull @Min(0) private Integer reorderLevel = 5;
    private boolean active = true;
    @NotNull private Long sellerId;
    @NotNull private Long categoryId;
}
