package com.restaurantsystem.salesmanagement.web;

import com.restaurantsystem.salesmanagement.service.ISaleService;
import com.restaurantsystem.salesmanagement.web.dto.SaleRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {
    private final ISaleService saleService;

    public SaleController(ISaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<String> save(@Valid @RequestBody SaleRequest saleRequest) {
        String id = saleService.save(saleRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(id);
    }
}
