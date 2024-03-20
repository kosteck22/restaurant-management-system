package com.restaurantsystem.stockmanagement.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record StockRequest(
        String description,
        @NotNull LocalDateTime date,
        @Valid @NotNull @Size(min = 1) List<ProductDto> products,
        @Valid @NotNull StockSourceDto stockSource
) {
}
