package com.restaurantsystem.stockmanagement.web.dto.invoice;


import lombok.Getter;

import java.math.BigDecimal;

@Getter
public record InvoiceProductDto (
        String name,
        String unitOfMeasure,
        BigDecimal netPrice,
        Integer vat,
        BigDecimal grossPrice
){
}
