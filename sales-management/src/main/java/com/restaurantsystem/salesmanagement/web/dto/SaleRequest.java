package com.restaurantsystem.salesmanagement.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record SaleRequest(
        @NotNull LocalDateTime date,
        @Valid @NotEmpty List<SaleItemRequest> items,
        @Valid @NotEmpty List<PaymentDetailRequest> paymentDetails
) {
}
