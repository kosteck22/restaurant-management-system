package com.restaurantsystem.stockmanagement.web.dto.invoice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public record InvoiceDto(
        String number,
        LocalDate createdAt,
        CompanyDto seller,
        CompanyDto buyer,
        OrderDto order
) {
}