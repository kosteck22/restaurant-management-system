package com.invoicesystem.invoicemanagement.service;

import com.invoicesystem.invoicemanagement.web.dto.ProductSearchSummaryDto;

import java.time.LocalDate;

public interface IProductService {
    ProductSearchSummaryDto search(String productName, LocalDate startDate, LocalDate endDate, Boolean includeTotals);
}
