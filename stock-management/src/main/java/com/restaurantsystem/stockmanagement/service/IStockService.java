package com.restaurantsystem.stockmanagement.service;

import com.restaurantsystem.stockmanagement.web.dto.StockRequest;

public interface IStockService {
    void addProductsToStock(StockRequest stockRequest);
}
