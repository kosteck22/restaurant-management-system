package com.restaurantsystem.recipe.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record IngredientDto(
        @NotBlank String name,
        @NotBlank String unitOfMeasure,
        @NotNull @Min(value = 0) BigDecimal quantity
) {
}
