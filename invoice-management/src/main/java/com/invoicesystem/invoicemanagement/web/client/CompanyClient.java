package com.invoicesystem.invoicemanagement.web.client;

import com.invoicesystem.invoicemanagement.web.dto.CompanyDto;
import com.invoicesystem.invoicemanagement.web.dto.request.CompanyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "company-service")
public interface CompanyClient {
    @GetMapping("/api/v1/companies/{id}")
    ResponseEntity<CompanyDto> get(@PathVariable("id") String id);

    @PostMapping("/api/v1/companies")
    ResponseEntity<String> saveOrGetId(@RequestBody CompanyRequest requestCompany);

    @GetMapping("/api/v1/companies/search/{name}")
    ResponseEntity<List<String>> getIdsForCompanyName(@PathVariable("name") String name);
}
