package com.invoicesystem.invoicemanagement.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invoicesystem.invoicemanagement.web.validation.MonetaryValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDto(
        @JsonProperty("name") @NotBlank String productName,
        @NotBlank String unitOfMeasure,
        @MonetaryValue(message = "Amount must have up to 2 decimal places") BigDecimal netPrice,
        @NotNull Integer vat,
        @MonetaryValue(message = "Amount must have up to 2 decimal places") BigDecimal grossPrice
) {
}
