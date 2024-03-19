package com.restaurantsystem.stockmanagement.web;

import com.restaurantsystem.stockmanagement.service.IStockService;
import com.restaurantsystem.stockmanagement.web.dto.StockRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/stock")
public class StockController {
    private final IStockService stockService;

    public StockController(IStockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("addition")
    public ResponseEntity<?> addProductsToStock(@Valid StockRequest stockRequest) {
        stockService.addProductsToStock(stockRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
