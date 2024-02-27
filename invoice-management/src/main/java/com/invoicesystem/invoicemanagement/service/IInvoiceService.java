package com.invoicesystem.invoicemanagement.service;

import com.invoicesystem.invoicemanagement.web.dto.InvoiceDto;
import com.invoicesystem.invoicemanagement.web.dto.TotalSumsDto;
import com.invoicesystem.invoicemanagement.web.dto.request.InvoiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IInvoiceService {
    String save(InvoiceRequest request);
    Page<InvoiceDto> getAsPage(Pageable pageable);
    InvoiceDto get(String id);
    TotalSumsDto summary(LocalDate startDate, LocalDate endDate, String companyName);
    void delete(String id);
    InvoiceDto update(InvoiceRequest invoiceRequest, String id);
}
