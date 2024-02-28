package com.restaurantsystem.articlemanagement.web.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builderx
public record ArticleDto(
        String name,
        String shortName,
        String category,
        Integer vat,
        BigDecimal grossPrice,
        boolean active
) {
}