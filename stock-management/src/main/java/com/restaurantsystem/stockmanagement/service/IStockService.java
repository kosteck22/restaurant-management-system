package com.restaurantsystem.stockmanagement.service;

import com.restaurantsystem.stockmanagement.web.dto.addToStock.AddToStockRequest;
import com.restaurantsystem.stockmanagement.web.dto.deduceFromStock.DeduceFromStockRequest;

public interface IStockService {
    void addProductsToStock(AddToStockRequest stockRequest);
    void deduceProductsFromStock(DeduceFromStockRequest stockRequest);
}
