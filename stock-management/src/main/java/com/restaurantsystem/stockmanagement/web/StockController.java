package com.restaurantsystem.stockmanagement.web;

import com.restaurantsystem.stockmanagement.entity.Stock;
import com.restaurantsystem.stockmanagement.service.IStockService;
import com.restaurantsystem.stockmanagement.web.dto.InventoryRequest;
import com.restaurantsystem.stockmanagement.web.dto.addToStock.AddToStockRequest;
import com.restaurantsystem.stockmanagement.web.dto.deduceFromStock.DeduceFromStockRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/stock")
public class StockController {
    private final IStockService stockService;

    public StockController(IStockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<Page<Stock>> getStocksAsPage(@PageableDefault(size = 20) Pageable pageable) {
        Page<Stock> stockPage = stockService.getStocksAsPage(pageable);

        if (stockPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.ok(stockPage);
    }

    @PostMapping("addition")
    public ResponseEntity<?> addProductsToStock(@Valid AddToStockRequest stockRequest) {
        stockService.addProductsToStock(stockRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PatchMapping("deduction")
    public ResponseEntity<?> deduceProductsFromStock(@Valid DeduceFromStockRequest stockRequest) {
        stockService.deduceProductsFromStock(stockRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("check-list")
    public ResponseEntity<?> addStockCheckList(@Valid InventoryRequest inventoryRequest) {
        String id = stockService.addStockCheckList(inventoryRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(id);
    }
}
