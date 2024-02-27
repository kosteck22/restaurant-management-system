package com.invoiceSystem.invoiceExtractor.web.client;

import com.invoiceSystem.invoiceExtractor.web.dto.InvoiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "invoice-management")
public interface InvoiceManagementClient {
    @PostMapping("/api/v1/invoices")
    ResponseEntity<String> save(InvoiceResponse invoiceRequest);
}
