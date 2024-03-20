package com.restaurantsystem.stockmanagement.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

public class ProductDetail {
    private String productId;

    @Field(targetType = DECIMAL128)
    private BigDecimal quantity;
}
