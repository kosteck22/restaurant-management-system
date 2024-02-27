package com.invoicesystem.invoicemanagement.entity.Order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class Order {
    private BigDecimal netPriceTotal;
    private BigDecimal vatTotal;
    private BigDecimal grossPriceTotal;
    private List<OrderDetails> orderDetails;
}
