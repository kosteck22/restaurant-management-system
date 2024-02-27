package com.invoiceSystem.invoiceExtractor.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Product {
    private String name;
    private String unitOfMeasure;
    private BigDecimal netPrice;
    private Integer vat;
    private BigDecimal grossPrice;
}
