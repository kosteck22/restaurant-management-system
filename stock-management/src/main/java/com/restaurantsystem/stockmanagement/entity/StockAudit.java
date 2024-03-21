package com.restaurantsystem.stockmanagement.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Builder
public class StockAudit {
    @Id
    private String id;

    @Indexed(unique = true)
    private LocalDateTime date;

    private String description;
    private StockAuditType type;

    private List<ProductDetail> productDetails;
}
