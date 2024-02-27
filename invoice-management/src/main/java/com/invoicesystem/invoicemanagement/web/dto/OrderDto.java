package com.invoicesystem.invoicemanagement.web.dto;

import com.invoicesystem.invoicemanagement.web.validation.MonetaryValue;
import jakarta.validation.Valid;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderDto(
        @Valid List<OrderDetailsDto> orderDetails,
        @MonetaryValue(message = "Amount must have up to 2 decimal places") BigDecimal netPriceTotal,
        @MonetaryValue(message = "Amount must have up to 2 decimal places") BigDecimal vatTotal,
        @MonetaryValue(message = "Amount must have up to 2 decimal places") BigDecimal grossPriceTotal
) {
}
