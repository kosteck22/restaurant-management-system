package com.restaurantsystem.recipe.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document
@Builder
@Getter
public class Recipe {
    @Id
    private String id;

    private List<Ingredient> ingredients;

    private String MenuItemId;
}
