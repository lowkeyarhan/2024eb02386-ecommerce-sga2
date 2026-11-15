package com.lowkeyarhan.ecommerce.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryForm {
    private Long id;
    @NotBlank @Size(max=80) private String categoryName;
    private Long parentCategoryId;
}
