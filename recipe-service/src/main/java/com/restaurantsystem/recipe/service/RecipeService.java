package com.restaurantsystem.recipe.service;

import com.restaurantsystem.recipe.entity.Ingredient;
import com.restaurantsystem.recipe.entity.Recipe;
import com.restaurantsystem.recipe.exception.DuplicateResourceException;
import com.restaurantsystem.recipe.exception.ResourceNotFoundException;
import com.restaurantsystem.recipe.service.dao.RecipeRepository;
import com.restaurantsystem.recipe.web.client.MenuClient;
import com.restaurantsystem.recipe.web.dto.MenuItemDto;
import com.restaurantsystem.recipe.web.dto.RecipeDto;
import com.restaurantsystem.recipe.web.dto.RecipeRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipeService implements IRecipeService {

    private final MenuClient menuClient;
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    @Override
    public RecipeDto get(String id) {
        return recipeRepository.findById(id)
                .map(recipeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe with id {%s} not found".formatted(id)));
    }

    @Override
    public List<RecipeDto> getRecipesByIds(List<String> menuItemIds) {
        return recipeRepository.findByMenuItemIdIn(menuItemIds).stream()
                .map(recipeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public String save(String menuItemId, RecipeRequest request) {
        ResponseEntity<MenuItemDto> menuItemResponse = menuClient.getById(menuItemId);
        if (menuItemResponse.getStatusCode() != HttpStatus.OK) {
            throw new ResourceNotFoundException("Menu item with id {%s} not found".formatted(menuItemId));
        }

        if (recipeRepository.findByMenuItemId(menuItemId).isPresent()) {
            throw new DuplicateResourceException("Recipe for menu item with id {%s} already exist".formatted(menuItemId));
        }

        List<Ingredient> ingredients = request.ingredients().stream()
                .map(i -> Ingredient.builder()
                        .name(i.name())
                        .unitOfMeasure(i.unitOfMeasure())
                        .quantity(i.quantity())
                        .build())
                .collect(Collectors.toList());

        Recipe recipe = Recipe.builder()
                .menuItemId(menuItemId)
                .ingredients(ingredients)
                .build();

        return recipeRepository.save(recipe).getId();
    }
}
