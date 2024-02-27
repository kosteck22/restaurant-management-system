package com.invoiceSystem.invoiceExtractor.web.dto;

import lombok.Builder;

@Builder
public record CompanyResponse (
        String name,
        String street,
        String city,
        String postalCode,
        String nip,
        String regon
) {
}
