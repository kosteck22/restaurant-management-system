package com.restaurantsystem.recipe.web.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record RecipeDto(
        String id,
        List<IngredientDto> ingredients,
        String menuItemId
) {
}
