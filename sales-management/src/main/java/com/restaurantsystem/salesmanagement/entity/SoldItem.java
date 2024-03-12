package com.restaurantsystem.salesmanagement.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Data
public class SoldItem {
    private String articleId;
    private String shortName;
    private int quantity;
    private Integer itemDiscount;
    private BigDecimal grossPrice;
    private BigDecimal netPrice;
    private VatRate vatRate;
    private BigDecimal vatTotal;
}
