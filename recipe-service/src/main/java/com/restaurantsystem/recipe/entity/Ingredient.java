package com.restaurantsystem.recipe.entity;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class Ingredient {
    private String name;
    private String unitOfMeasure;
    private BigDecimal quantity;
}
