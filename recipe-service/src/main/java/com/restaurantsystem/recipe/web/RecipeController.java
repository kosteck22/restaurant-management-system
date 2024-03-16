package com.restaurantsystem.recipe.web;

import com.restaurantsystem.recipe.service.IRecipeService;
import com.restaurantsystem.recipe.web.dto.RecipeRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    private final IRecipeService recipeService;

    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/menu-items/{id}")
    public ResponseEntity<?> save(@PathVariable(name = "id") String menuItemId,
                                  @Valid @RequestBody RecipeRequest recipeRequest) {
        String id = recipeService.save(menuItemId, recipeRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(id);
    }
}
