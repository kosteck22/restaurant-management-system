package com.restaurantsystem.stockmanagement.web.client;

import com.restaurantsystem.stockmanagement.web.dto.recipe.RecipeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "recipe-service")
public interface RecipeClient {
    @PostMapping("api/v1/recipes/find-by-menu-item-ids")
    public ResponseEntity<List<RecipeDto>> getRecipesByIds(@RequestBody List<String> menuItemIds);
}
