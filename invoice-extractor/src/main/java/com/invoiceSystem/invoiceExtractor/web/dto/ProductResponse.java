package com.invoiceSystem.invoiceExtractor.web.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse (
        String name,
        String unitOfMeasure,
        BigDecimal netPrice,
        Integer vat
) {
}
