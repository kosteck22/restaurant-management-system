package com.restaurantsystem.recipe.web;

import com.restaurantsystem.recipe.service.IRecipeService;
import com.restaurantsystem.recipe.web.dto.RecipeDto;
import com.restaurantsystem.recipe.web.dto.RecipeRequest;
import feign.Response;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    private final IRecipeService recipeService;

    public RecipeController(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> get(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(recipeService.get(id));
    }

    @PostMapping("/find-by-menu-item-ids")
    public ResponseEntity<List<RecipeDto>> getRecipesByIds(@RequestBody List<String> menuItemIds) {
        return ResponseEntity.ok(recipeService.getRecipesByIds(menuItemIds));
    }

    @PostMapping("/menu-items/{id}")
    public ResponseEntity<String> save(@PathVariable(name = "id") String menuItemId,
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
