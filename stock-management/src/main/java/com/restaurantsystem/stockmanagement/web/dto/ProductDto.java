package com.restaurantsystem.stockmanagement.web.dto;

import com.restaurantsystem.stockmanagement.entity.ProductCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductDto(
        @NotBlank String name,
        @NotNull @DecimalMin(value = "0", message = "Quantity must be greater than 0") BigDecimal quantity,
        @NotBlank String unitOfMeasure,
        ProductCategory category
) {
}
