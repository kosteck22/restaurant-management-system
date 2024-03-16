package com.restaurantsystem.stockmanagement.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.MediaSize;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;
}
