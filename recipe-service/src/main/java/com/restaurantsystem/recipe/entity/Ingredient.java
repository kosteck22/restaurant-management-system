package com.restaurantsystem.recipe.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class Ingredient {
    private String name;
    private String unitOfMeasure;
    private BigDecimal quantity;
}
