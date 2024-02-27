package com.invoiceSystem.invoiceExtractor.web.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderDetailsResponse(
        Integer positionNumber,
        ProductResponse product,
        BigDecimal quantity,
        Integer discount
)  {
}
