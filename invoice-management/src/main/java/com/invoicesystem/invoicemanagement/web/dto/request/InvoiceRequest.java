package com.invoicesystem.invoicemanagement.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InvoiceRequest (
        @NotBlank String number,
        @NotNull LocalDate createdAt,
        @NotNull @Valid CompanyRequest seller,
        @NotNull @Valid CompanyRequest buyer,
        @NotNull @Valid OrderRequest order
){
}
