package com.lowkeyarhan.ecommerce.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerForm {
    private Long id;
    @NotBlank @Size(max=60) private String firstName;
    @NotBlank @Size(max=60) private String lastName;
    @NotBlank @Email @Size(max=254) private String email;
    @Size(max=30) private String phone;
}
