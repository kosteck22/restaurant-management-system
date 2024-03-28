package com.restaurantsystem.menumanagement.web.dto;

import com.restaurantsystem.menumanagement.web.validation.MonetaryValue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MenuItemRequest(
        @NotBlank String name,
        @NotBlank String shortName,
        String description,
        @NotBlank String category,
        @NotNull Integer vat,
        @NotNull @MonetaryValue @DecimalMin(value = "0.01", message = "Gross price must be greater than 0") BigDecimal grossPrice,
        boolean active
) {
}
