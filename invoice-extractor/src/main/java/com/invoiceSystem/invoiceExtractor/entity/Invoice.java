package com.invoiceSystem.invoiceExtractor.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Invoice {
    private String number;
    private LocalDate createdAt;
    private Company seller;
    private Company buyer;
    private Order order;
}
