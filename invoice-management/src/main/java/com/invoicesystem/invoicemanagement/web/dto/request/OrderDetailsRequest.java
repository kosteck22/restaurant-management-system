package com.invoicesystem.invoicemanagement.web.dto.request;

import com.invoicesystem.invoicemanagement.web.dto.ProductDto;
import com.invoicesystem.invoicemanagement.web.validation.MonetaryValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

public record OrderDetailsRequest (
        Integer positionNumber,
        @NotNull @Valid ProductRequest product,
        @NotNull @DecimalMin(value = "0.001", message = "Quantity must be greater than 0") BigDecimal quantity,
        @Range(min = 0, max = 100) Integer discount
) {
}
