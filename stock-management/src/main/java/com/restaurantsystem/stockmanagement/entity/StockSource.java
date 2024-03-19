package com.restaurantsystem.stockmanagement.entity;

import lombok.Getter;

import java.util.Optional;

@Getter
public class StockSource {
    private String sourceId;
    private SourceType sourceType;

    public Optional<String> getSourceId() {
        return Optional.of(sourceId);
    }
}
