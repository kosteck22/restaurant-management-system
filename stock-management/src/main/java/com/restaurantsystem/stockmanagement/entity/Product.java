package com.restaurantsystem.stockmanagement.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Builder
public class Product {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private String unitOfMeasure;

    private ProductCategory category;
}
