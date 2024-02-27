package com.invoicesystem.invoicemanagement.entity.Order;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Product {
    private String name;
    private String unitOfMeasure;
    private BigDecimal netPrice;
    private VatRate vat;
    private BigDecimal grossPrice;
}
