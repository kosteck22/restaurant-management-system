package com.restaurantsystem.stockmanagement.web.client;

import com.restaurantsystem.stockmanagement.web.dto.invoice.InvoiceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "invoice-management")
public interface InvoiceManagementClient {

    @GetMapping("api/v1/invoices/{id}")
    ResponseEntity<InvoiceDto> get(@PathVariable("id") String id);
}

