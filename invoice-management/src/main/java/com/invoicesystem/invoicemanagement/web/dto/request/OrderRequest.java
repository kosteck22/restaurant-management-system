package com.invoicesystem.invoicemanagement.web.dto.request;

import jakarta.validation.Valid;

import java.util.List;

public record OrderRequest (
        @Valid List<OrderDetailsRequest> orderDetails
) {
}
