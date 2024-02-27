package com.invoiceSystem.invoiceExtractor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvoiceParseException extends RuntimeException{
    public InvoiceParseException(String msg) {
        super(msg);
    }
}
