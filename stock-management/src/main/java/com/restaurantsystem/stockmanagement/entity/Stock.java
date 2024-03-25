package com.restaurantsystem.stockmanagement.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@Document
@Builder
@Getter @Setter
public class Stock {
    @Id
    private String id;

    private String productId;
    private String productName;

    @Field(targetType = DECIMAL128)
    private BigDecimal quantity;
}
