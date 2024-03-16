package com.restaurantsystem.recipe.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record IngredientDto(
        @NotBlank String name,
        @NotBlank String unitOfMeasure,
        @NotNull @Min(value = 0) BigDecimal quantity
) {
}
