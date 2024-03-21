package com.restaurantsystem.stockmanagement.service;

import com.restaurantsystem.stockmanagement.entity.Stock;
import com.restaurantsystem.stockmanagement.web.dto.InventoryRequest;
import com.restaurantsystem.stockmanagement.web.dto.addToStock.AddToStockRequest;
import com.restaurantsystem.stockmanagement.web.dto.deduceFromStock.DeduceFromStockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStockService {
    void addProductsToStock(AddToStockRequest stockRequest);
    void deduceProductsFromStock(DeduceFromStockRequest stockRequest);
    Page<Stock> getStocksAsPage(Pageable pageable);
    String addStockCheckList(InventoryRequest inventoryRequest);
}
