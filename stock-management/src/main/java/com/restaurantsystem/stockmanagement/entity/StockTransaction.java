package com.restaurantsystem.stockmanagement.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@Document
@Builder
public class StockTransaction {
    @Id
    private String id;
    private LocalDateTime date;
    private String description;
    private String productId;

    @Field(targetType = DECIMAL128)
    private BigDecimal quantity;

    private StockSource source;
    private TransactionType transactionType;
}
