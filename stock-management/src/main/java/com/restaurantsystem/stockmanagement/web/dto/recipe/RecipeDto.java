package com.restaurantsystem.stockmanagement.web.dto.recipe;

import java.util.List;

public record RecipeDto(
        String id,
        List<IngredientDto> ingredients,
        String menuItemId
) {
}
