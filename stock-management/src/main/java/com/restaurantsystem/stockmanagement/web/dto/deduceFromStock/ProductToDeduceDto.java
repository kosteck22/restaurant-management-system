package com.restaurantsystem.stockmanagement.web.dto.deduceFromStock;

import com.restaurantsystem.stockmanagement.entity.ProductCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductToDeduceDto (
        @NotBlank String productId,
        @NotNull @DecimalMin(value = "0", message = "Quantity must be greater than 0") BigDecimal quantity
) {
}