package com.invoiceSystem.invoiceExtractor.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDetails {
    private Integer positionNumber;
    private Product product;
    private BigDecimal quantity;
    private Integer discount;
    private BigDecimal netTotal;
    private BigDecimal vatTotal;
    private BigDecimal grossPriceTotal;

}
