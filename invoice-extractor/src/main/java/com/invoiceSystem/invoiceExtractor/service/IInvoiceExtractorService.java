package com.invoiceSystem.invoiceExtractor.service;

import com.invoiceSystem.invoiceExtractor.entity.Invoice;
import com.invoiceSystem.invoiceExtractor.service.invoiceExtractor.InvoiceImageExtractionType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IInvoiceExtractorService {
    String extractInvoice(MultipartFile file, InvoiceImageExtractionType company) throws IOException;
}
