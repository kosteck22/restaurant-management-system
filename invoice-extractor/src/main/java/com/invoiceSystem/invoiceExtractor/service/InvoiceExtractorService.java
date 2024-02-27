package com.invoiceSystem.invoiceExtractor.service;

import com.invoiceSystem.invoiceExtractor.entity.Invoice;
import com.invoiceSystem.invoiceExtractor.exception.InvoiceValidationException;
import com.invoiceSystem.invoiceExtractor.service.invoiceExtractor.InvoiceDataExtractor;
import com.invoiceSystem.invoiceExtractor.service.invoiceExtractor.InvoiceImageExtractionType;
import com.invoiceSystem.invoiceExtractor.web.client.InvoiceManagementClient;
import com.invoiceSystem.invoiceExtractor.web.dto.InvoiceResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class InvoiceExtractorService implements IInvoiceExtractorService {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceExtractorService.class);

    private final InvoiceMapper invoiceMapper;
    private final InvoiceManagementClient invoiceManagementClient;

    public InvoiceExtractorService(InvoiceMapper invoiceMapper, InvoiceManagementClient invoiceManagementClient) {
        this.invoiceMapper = invoiceMapper;
        this.invoiceManagementClient = invoiceManagementClient;
    }

    public String extractInvoice(MultipartFile file, InvoiceImageExtractionType company) throws IOException {
        InvoiceDataExtractor extractor = company.getExtractor();
        byte[] bytes = file.getBytes();

        Invoice invoice = extractor.extractInvoiceData(bytes);
        return getInvoiceId(invoice);
    }

    private String getInvoiceId(Invoice invoice) {
        logger.info("Trying to save extracted invoice: {}", invoice.toString());
        try {
            InvoiceResponse invoiceResponse = invoiceMapper.toDto(invoice);
            return invoiceManagementClient.save(invoiceResponse).getBody();
        } catch (FeignException e) {
            logger.error("Couldn't save invoice to management service", e);
            throw new InvoiceValidationException("Couldn't save invoice to management service", invoice);
        }}

}
