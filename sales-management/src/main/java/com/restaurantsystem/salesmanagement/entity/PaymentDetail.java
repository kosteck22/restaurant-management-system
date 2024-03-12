package com.restaurantsystem.salesmanagement.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class PaymentDetail {
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
}
