package com.invoiceSystem.invoiceExtractor.service.invoiceExtractor;

import com.invoiceSystem.invoiceExtractor.entity.Invoice;
import org.springframework.web.multipart.MultipartFile;

public interface InvoiceDataExtractor {
    Invoice extractInvoiceData(byte[] img);
}
