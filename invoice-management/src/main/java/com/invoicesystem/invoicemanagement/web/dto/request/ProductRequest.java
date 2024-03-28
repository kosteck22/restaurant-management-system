package com.invoicesystem.invoicemanagement.web.dto.request;

import com.invoicesystem.invoicemanagement.web.validation.MonetaryValue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest (
        @NotBlank String name,
        @NotBlank String unitOfMeasure,
        @NotNull @DecimalMin(value = "0") @MonetaryValue(message = "Amount must have up to 2 decimal places") BigDecimal netPrice,
        @NotNull Integer vat
) {
}
