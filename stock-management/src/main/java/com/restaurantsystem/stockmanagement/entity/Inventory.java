package com.restaurantsystem.stockmanagement.entity;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document
public class Inventory {
    @Id
    private String id;

    @Indexed(unique = true)
    private LocalDate date;

    private String description;

    private List<ProductDetail> productDetails;
}
