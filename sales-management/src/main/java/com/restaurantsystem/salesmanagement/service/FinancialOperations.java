package com.restaurantsystem.salesmanagement.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinancialOperations {
    public static class ItemResult {
        public BigDecimal netPrice;
        public BigDecimal vatTotal;
        public BigDecimal grossTotal;

        public ItemResult(BigDecimal netPrice, BigDecimal vatTotal, BigDecimal grossTotal) {
            this.netPrice = netPrice;
            this.vatTotal = vatTotal;
            this.grossTotal = grossTotal;
        }
    }

    public static ItemResult calculateTotals(int q, BigDecimal grossPrice, Integer vRate, Integer percentageDiscount) {
        BigDecimal grossTotal;
        BigDecimal netTotal;
        BigDecimal vatTotal;

        BigDecimal quantity = BigDecimal.valueOf(q);
        BigDecimal vatRate = BigDecimal.valueOf(vRate);
        BigDecimal discount = percentageDiscount == null ? BigDecimal.ZERO : BigDecimal.valueOf(percentageDiscount);

        // Apply discount
        BigDecimal discountedGrossPrice = grossPrice.subtract(grossPrice.multiply(discount).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
        grossTotal = discountedGrossPrice.multiply(quantity);

        // Calculate VAT and net price
        BigDecimal onePlusVatRate = BigDecimal.ONE.add(vatRate.divide(new BigDecimal("100")));
        netTotal = grossTotal.divide(onePlusVatRate, 2, RoundingMode.HALF_UP);
        vatTotal = grossTotal.subtract(netTotal);

        return new ItemResult(netTotal, vatTotal, grossTotal);
    }
}
