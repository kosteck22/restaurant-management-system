package com.invoicesystem.invoicemanagement.web.dto;

import java.math.BigDecimal;
import java.util.Map;

public record TotalSumsDto(
        BigDecimal netPriceTotal,
        BigDecimal vatTotal,
        BigDecimal grossPriceTotal,
        Map<Integer, BigDecimal> vatTotalByRate
) {
}
