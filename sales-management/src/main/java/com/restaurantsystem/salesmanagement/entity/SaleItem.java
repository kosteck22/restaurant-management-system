package com.restaurantsystem.salesmanagement.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class SaleItem {
    private String menuItemId;
    private String shortName;
    private int quantity;
    private Integer itemDiscount;
    private BigDecimal grossPrice;
    private BigDecimal grossPriceTotal;
    private BigDecimal netPriceTotal;
    private VatRate vatRate;
    private BigDecimal vatTotal;
}
