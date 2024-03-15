package com.restaurantsystem.salesmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class VatDetail {
    private VatRate vatRate;
    private BigDecimal vatAmount;
}
