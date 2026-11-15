package com.lowkeyarhan.ecommerce.catalog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SellerForm {
    private Long id;
    @NotBlank @Size(max=120) private String sellerName;
    @NotBlank @Email @Size(max=254) private String contactEmail;
    private String status = "ACTIVE";
}
