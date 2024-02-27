package com.invoiceSystem.invoiceExtractor.web.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record InvoiceResponse (
        String number,
        LocalDate createdAt,
        CompanyResponse seller,
        CompanyResponse buyer,
        OrderResponse order
){
}