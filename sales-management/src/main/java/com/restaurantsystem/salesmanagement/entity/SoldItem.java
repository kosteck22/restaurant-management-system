package com.restaurantsystem.salesmanagement.entity;

import java.math.BigDecimal;

public class SoldItem {
    private String articleId;
    private int quantity;
    private BigDecimal itemDiscount;
    private BigDecimal grossPrice;
    private VatRate vatRate;
    private BigDecimal vatTotal;
}
