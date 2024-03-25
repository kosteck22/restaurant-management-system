package com.restaurantsystem.stockmanagement.web.dto.recipe;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public record IngredientDto(
        @NotBlank String name,
        @NotBlank String unitOfMeasure,
        @NotNull @DecimalMin(value = "0", message = "Quantity must be greater than 0") BigDecimal quantity
) {
}
