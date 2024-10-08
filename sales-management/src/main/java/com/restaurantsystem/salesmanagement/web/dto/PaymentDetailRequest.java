package com.restaurantsystem.salesmanagement.web.dto;

import com.restaurantsystem.salesmanagement.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentDetailRequest(
        @NotNull PaymentMethod paymentMethod,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount
) {
}
