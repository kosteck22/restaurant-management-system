package com.restaurantsystem.menumanagement.web.dto;

import com.restaurantsystem.menumanagement.web.validation.MonetaryValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ArticleRequest(
        @NotBlank String name,
        @NotBlank String shortName,
        @NotBlank String category,
        Integer vat,
        @NotNull @MonetaryValue BigDecimal grossPrice,
        boolean active
) {
}
