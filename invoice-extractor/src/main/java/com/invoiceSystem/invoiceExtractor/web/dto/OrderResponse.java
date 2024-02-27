package com.invoiceSystem.invoiceExtractor.web.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponse(
        List<OrderDetailsResponse> orderDetails
) {
}
