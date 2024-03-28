package com.restaurantsystem.stockmanagement.web.dto.recipe;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
public record RecipeDto(
        String id,
        List<IngredientDto> ingredients,
        String menuItemId
) {
}

