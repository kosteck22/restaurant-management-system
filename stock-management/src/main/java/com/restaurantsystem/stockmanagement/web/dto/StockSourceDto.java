package com.restaurantsystem.stockmanagement.web.dto;

import com.restaurantsystem.stockmanagement.entity.SourceType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public record StockSourceDto(
        String sourceId,
        @NotNull SourceType sourceType
) {
    public Optional<String> getSourceId() {
        return Optional.ofNullable(sourceId);
    }
}
