package com.invoicesystem.invoicemanagement.web.dto;

import com.invoicesystem.invoicemanagement.entity.Order.Product;
import com.invoicesystem.invoicemanagement.web.validation.MonetaryValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderDetailsDto(
        Integer positionNumber,
        @NotNull @Valid ProductDto product,
        @NotNull @DecimalMin(value = "0.01", message = "Quantity must be greater than 0") BigDecimal quantity,
        @MonetaryValue(message = "Net price must have up to 2 decimal places") BigDecimal netPriceTotal,
        @MonetaryValue(message = "Vat total must have up to 2 decimal places") BigDecimal vatTotal,
        @MonetaryValue(message = "Gross price must have up to 2 decimal places") BigDecimal grossPriceTotal
) {
}
