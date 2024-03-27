package com.restaurantsystem.stockmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
public class StockSource {
    private String sourceId;
    private SourceType sourceType;

    public Optional<String> getSourceId() {
        return Optional.of(sourceId);
    }
}
