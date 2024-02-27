package com.invoicesystem.invoicemanagement.web.dto.request;

import com.invoicesystem.invoicemanagement.web.validation.ValidNip;
import jakarta.validation.constraints.NotBlank;

public record CompanyRequest (
        @NotBlank String name,
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String postalCode,
        @ValidNip String nip,
        String regon
) {
}