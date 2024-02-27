package com.invoiceSystem.invoiceExtractor.web;


import com.invoiceSystem.invoiceExtractor.entity.Invoice;
import com.invoiceSystem.invoiceExtractor.service.IInvoiceExtractorService;
import com.invoiceSystem.invoiceExtractor.service.invoiceExtractor.InvoiceImageExtractionType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ExtractorController {
    private final IInvoiceExtractorService invoiceImageService;

    public ExtractorController(IInvoiceExtractorService invoiceProcessingService) {
        this.invoiceImageService = invoiceProcessingService;
    }

    @PostMapping(
            value = "/extract/invoice",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> extractInvoice(
            @RequestParam(name = "file") MultipartFile file,
            @RequestParam(name = "company") InvoiceImageExtractionType company) throws IOException {
        String id = invoiceImageService.extractInvoice(file, company);
        return ResponseEntity.ok(id);
    }
}