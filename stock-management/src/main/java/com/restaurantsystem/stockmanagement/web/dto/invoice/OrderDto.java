package com.restaurantsystem.stockmanagement.web.dto.invoice;

import jakarta.validation.Valid;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public record OrderDto(
        List<OrderDetailsDto> orderDetails,
        BigDecimal netPriceTotal,
        BigDecimal vatTotal,
        BigDecimal grossPriceTotal
) {
}
