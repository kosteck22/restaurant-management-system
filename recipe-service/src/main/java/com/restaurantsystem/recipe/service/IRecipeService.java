package com.restaurantsystem.recipe.service;

import com.restaurantsystem.recipe.web.dto.RecipeDto;
import com.restaurantsystem.recipe.web.dto.RecipeRequest;

import java.util.List;

public interface IRecipeService {
    String save(String menuItemId, RecipeRequest request);
    RecipeDto get(String id);
    List<RecipeDto> getRecipesByIds(List<String> menuItemIds);
}
