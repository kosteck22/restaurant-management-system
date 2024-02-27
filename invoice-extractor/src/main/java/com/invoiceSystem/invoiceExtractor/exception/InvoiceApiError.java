package com.invoiceSystem.invoiceExtractor.exception;

import com.invoiceSystem.invoiceExtractor.entity.Invoice;

public record InvoiceApiError (
        ApiError error,
        Invoice invoice
){
}
