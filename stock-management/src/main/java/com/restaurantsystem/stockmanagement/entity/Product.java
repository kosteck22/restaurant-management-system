package com.restaurantsystem.stockmanagement.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.MediaSize;

@Document
@Getter
@Builder
public class Product {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private String unitOfMeasure;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
}
