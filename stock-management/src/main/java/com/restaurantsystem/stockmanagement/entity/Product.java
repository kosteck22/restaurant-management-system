package com.restaurantsystem.stockmanagement.entity;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.MediaSize;

@Document
public class Product {
    @Id
    private String id;
    private String name;
    private String unitOfMeasure;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
}
