package com.restaurantsystem.stockmanagement.web.dto.invoice;

import jakarta.validation.constraints.NotBlank;

public record CompanyDto(
        String id,
        String name,
        String street,
        String city,
        String postalCode,
        String nip
) {
}