package com.invoicesystem.invoicemanagement.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record InvoiceDto(
        @NotBlank String number,
        @NotNull LocalDate createdAt,
        @NotNull @Valid CompanyDto seller,
        @NotNull @Valid CompanyDto buyer,
        @NotNull @Valid OrderDto order
) {
}
