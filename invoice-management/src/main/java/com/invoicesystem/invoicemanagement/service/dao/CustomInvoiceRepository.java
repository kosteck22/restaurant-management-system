package com.invoicesystem.invoicemanagement.service.dao;

import com.invoicesystem.invoicemanagement.entity.Invoice;
import com.invoicesystem.invoicemanagement.web.dto.ProductSearchDto;

import java.time.LocalDate;
import java.util.List;

public interface CustomInvoiceRepository {
    List<Invoice> findInvoicesByCriteria(LocalDate startDate, LocalDate endDate, List<String> sellerIds);
    List<ProductSearchDto> findProductsByCriteria(LocalDate startDate, LocalDate endDate, String productName);

}
