package com.invoicesystem.invoicemanagement.web.dto.request;

import com.invoicesystem.invoicemanagement.web.validation.ValidNip;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CompanyRequest (
        @NotBlank String name,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String postalCode,
        @NotNull @ValidNip String nip,
        String regon
) {
}