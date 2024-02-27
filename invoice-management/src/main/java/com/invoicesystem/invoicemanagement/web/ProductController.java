package com.invoicesystem.invoicemanagement.web;

import com.invoicesystem.invoicemanagement.service.IProductService;
import com.invoicesystem.invoicemanagement.web.dto.ProductSearchSummaryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public ResponseEntity<ProductSearchSummaryDto> search(@RequestParam(name = "product") String productName,
                                                          @RequestParam(name = "from", required = false) LocalDate startDate,
                                                          @RequestParam(name = "to", required = false) LocalDate endDate,
                                                          @RequestParam(name = "includeTotals", required = false) boolean includeTotals) {
        return ResponseEntity.ok(productService.search(productName, startDate, endDate, includeTotals));
    }

}
