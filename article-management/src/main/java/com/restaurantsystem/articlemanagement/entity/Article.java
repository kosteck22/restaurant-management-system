package com.restaurantsystem.articlemanagement.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Builder
@Data
public class Article {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @Indexed(unique = true)
    private String shortName;

    private String category;

    private VatRate vat;
    private BigDecimal grossPrice;

    private boolean active;
}
