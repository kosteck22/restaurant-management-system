package com.restaurantsystem.recipe.service;

import com.restaurantsystem.recipe.entity.Ingredient;
import com.restaurantsystem.recipe.entity.Recipe;
import com.restaurantsystem.recipe.web.dto.IngredientDto;
import com.restaurantsystem.recipe.web.dto.RecipeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeMapper {
    RecipeDto toDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .ingredients(getIngredientsDtos(recipe.getIngredients()))
                .menuItemId(recipe.getMenuItemId())
                .build();
    }

    private List<IngredientDto> getIngredientsDtos(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(i -> IngredientDto.builder()
                        .name(i.getName())
                        .unitOfMeasure(i.getUnitOfMeasure())
                        .quantity(i.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}
