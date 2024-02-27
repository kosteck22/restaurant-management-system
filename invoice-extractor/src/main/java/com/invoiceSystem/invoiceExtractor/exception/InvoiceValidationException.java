package com.invoiceSystem.invoiceExtractor.exception;


import com.invoiceSystem.invoiceExtractor.entity.Invoice;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvoiceValidationException extends RuntimeException{
    private final Invoice invoice;

    public InvoiceValidationException(String msg, Invoice invoice) {
        super(msg);
        this.invoice = invoice;
    }
}
