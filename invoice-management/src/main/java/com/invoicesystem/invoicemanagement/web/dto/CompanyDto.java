package com.invoicesystem.invoicemanagement.web.dto;

import com.invoicesystem.invoicemanagement.web.validation.ValidNip;
import jakarta.validation.constraints.NotBlank;

public record CompanyDto(
        String id,
        @NotBlank String name,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String postalCode,
        @ValidNip String nip
) {
    public CompanyDto(String id) {
        this(id, null, null, null, null, null);
    }
}
