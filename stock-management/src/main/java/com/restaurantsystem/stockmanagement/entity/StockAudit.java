package com.restaurantsystem.stockmanagement.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private StockAuditType stockAuditType;
    private List<ProductDetail> productDetails;
}
