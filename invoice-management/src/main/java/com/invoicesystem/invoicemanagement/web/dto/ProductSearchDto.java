package com.invoicesystem.invoicemanagement.web.dto;

import com.invoicesystem.invoicemanagement.entity.Order.VatRate;
import lombok.Builder;

import java.math.BigDecimal;


@Builder
public record ProductSearchDto(
        String invoiceNumber,
        String name,
        BigDecimal quantity,
        BigDecimal netPrice,
        Integer discount,
        BigDecimal netTotal,
        VatRate vat,
        BigDecimal vatTotal,
        BigDecimal grossPriceTotal
) {
    public Integer getVat() {
        return vat.getRate();
    }
}
