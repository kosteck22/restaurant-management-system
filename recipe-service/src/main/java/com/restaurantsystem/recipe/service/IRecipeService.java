package com.restaurantsystem.recipe.service;

import com.restaurantsystem.recipe.web.dto.RecipeDto;
import com.restaurantsystem.recipe.web.dto.RecipeRequest;

public interface IRecipeService {
    String save(String menuItemId, RecipeRequest request);
    RecipeDto get(String id);
}
