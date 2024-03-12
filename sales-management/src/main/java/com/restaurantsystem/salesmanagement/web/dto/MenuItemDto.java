package com.restaurantsystem.salesmanagement.web.dto;

import java.math.BigDecimal;

public record MenuItemDto(
        String id,
        String name,
        String shortName,
        String description,
        String category,
        Integer vat,
        BigDecimal grossPrice,
        boolean active
) {
}

