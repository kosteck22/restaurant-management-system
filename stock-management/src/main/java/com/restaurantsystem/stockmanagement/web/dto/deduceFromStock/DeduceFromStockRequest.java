package com.restaurantsystem.stockmanagement.web.dto.deduceFromStock;

import com.restaurantsystem.stockmanagement.web.dto.StockSourceDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record DeduceFromStockRequest(
        String description,
        @NotNull LocalDateTime date,
        @Valid @NotNull @Size(min = 1, max = 300) List<ProductDetailDto> products,
        @Valid @NotNull StockSourceDto stockSource
) {
}
