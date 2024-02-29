package com.restaurantsystem.menumanagement.web.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MenuItemDto(
        String name,
        String shortName,
        String description,
        String category,
        Integer vat,
        BigDecimal grossPrice,
        boolean active
) {
}
