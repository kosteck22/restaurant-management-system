package com.restaurantsystem.stockmanagement.web.dto;

import com.restaurantsystem.stockmanagement.web.dto.deduceFromStock.ProductDetailDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record InventoryRequest(
        @NotNull LocalDateTime date,
        String description,
        @Valid @NotNull @Size(min = 1, max = 400)List<ProductDetailDto> products
) {
}
