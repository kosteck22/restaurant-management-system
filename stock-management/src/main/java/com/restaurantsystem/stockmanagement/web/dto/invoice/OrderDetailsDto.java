package com.restaurantsystem.stockmanagement.web.dto.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;


public record OrderDetailsDto(
        Integer positionNumber,
        InvoiceProductDto product,
        BigDecimal quantity,
        BigDecimal netPriceTotal,
        BigDecimal vatTotal,
        BigDecimal grossPriceTotal
) {
}
