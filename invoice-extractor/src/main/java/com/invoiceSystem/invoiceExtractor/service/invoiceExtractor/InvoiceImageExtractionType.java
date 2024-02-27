package com.invoiceSystem.invoiceExtractor.service.invoiceExtractor;


import com.invoiceSystem.invoiceExtractor.ApplicationContextProvider;

public enum InvoiceImageExtractionType {
    KUCHNIE_SWIATA("kuchnieSwiataExtractor"),
    AGROHURT("agroHurtExtractor");

    private final String extractorName;

    InvoiceImageExtractionType(String extractorName) {
        this.extractorName = extractorName;
    }

    public InvoiceDataExtractor getExtractor() {
        return (InvoiceDataExtractor) ApplicationContextProvider.getApplicationContext().getBean(extractorName);
    }
}
