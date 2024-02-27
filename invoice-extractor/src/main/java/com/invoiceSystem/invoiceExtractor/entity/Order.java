package com.invoiceSystem.invoiceExtractor.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Order {
    private List<OrderDetails> orderDetails;
    private BigDecimal netPriceTotal;
    private BigDecimal vatTotal;
    private BigDecimal grossPriceTotal;
}
