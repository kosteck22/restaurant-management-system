package com.restaurantsystem.recipe.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RecipeRequest(
        @Valid @NotEmpty List<IngredientDto> ingredients
) {
}
