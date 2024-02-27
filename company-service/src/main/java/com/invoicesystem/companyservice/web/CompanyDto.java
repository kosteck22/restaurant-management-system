package com.invoicesystem.companyservice.web;

import lombok.Builder;

@Builder
public record CompanyDto(
        String name,
        String street,
        String city,
        String postalCode,
        String nip,
        String regon
) {
}
