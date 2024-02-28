package com.restaurantsystem.articlemanagement.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
public class Article {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @Indexed(unique = true)
    private String shortName;

    private String category;

    private Integer vat;
    private BigDecimal grossPrice;

    private Boolean active;
}
