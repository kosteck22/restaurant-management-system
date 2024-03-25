package com.restaurantsystem.stockmanagement.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Builder
@Getter
public class StockTransaction {
    @Id
    private String id;
    private LocalDateTime date;
    private String description;

    private List<ProductDetail> productDetails;
    private StockSource source;

    private TransactionType transactionType;

    public StockTransaction(LocalDateTime date, String description, List<ProductDetail> products, StockSource stockSource, TransactionType transactionType) {
        this.date = date;
        this.description = description;
        this.productDetails = products;
        this.setStockSource(stockSource);
        this.transactionType = transactionType;
    }

    public void setStockSource(StockSource stockSource) {
        if (transactionType == TransactionType.ADD && (stockSource.getSourceType() == SourceType.SALE_REQUEST || stockSource.getSourceType() == SourceType.WASTE)) {
            throw new IllegalArgumentException("Invalid source type for ADD transaction");
        } else if (transactionType == TransactionType.DEDUCE && stockSource.getSourceType() == SourceType.INVOICE) {
            throw new IllegalArgumentException("Invalid source type for DEDUCE transaction");
        }
        // Validate sourceId for Invoice and Sale_Request
        if ((stockSource.getSourceType() == SourceType.INVOICE || stockSource.getSourceType() == SourceType.SALE_REQUEST) && stockSource.getSourceId().isEmpty()) {
            throw new IllegalArgumentException("Source ID is required for INVOICE and SALE_REQUEST source types.");
        }
        this.source = stockSource;
    }
}
