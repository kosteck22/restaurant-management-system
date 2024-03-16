package com.restaurantsystem.recipe.service.dao;

import com.restaurantsystem.recipe.entity.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    Optional<Recipe> findByMenuItemId(String menuItemId);
    List<Recipe> findByMenuItemIdIn(List<String> ids);
}
