package com.invoicesystem.invoicemanagement.service;

import com.invoicesystem.invoicemanagement.service.dao.InvoiceRepository;
import com.invoicesystem.invoicemanagement.web.dto.ProductSearchDto;
import com.invoicesystem.invoicemanagement.web.dto.ProductSearchSummaryDto;
import com.invoicesystem.invoicemanagement.web.dto.TotalSumsDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.invoicesystem.invoicemanagement.service.FinanceUtilities.sum;
import static com.invoicesystem.invoicemanagement.service.FinanceUtilities.validateDate;

@Service
public class ProductService implements IProductService {

    private final InvoiceRepository invoiceRepository;

    public ProductService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public ProductSearchSummaryDto search(String productName, LocalDate startDate, LocalDate endDate, Boolean includeTotals) {
        validateDate(startDate, endDate);
        List<ProductSearchDto> products = invoiceRepository.findProductsByCriteria(startDate, endDate, productName);
        TotalSumsDto totals = includeTotals ? calculateProductTotals(products) : null;

        return new ProductSearchSummaryDto(products, totals);
    }

    private TotalSumsDto calculateProductTotals(List<ProductSearchDto> products) {
        BigDecimal netPriceTotal = sum(products, ProductSearchDto::netTotal);
        BigDecimal vatTotal = sum(products, ProductSearchDto::vatTotal);
        BigDecimal grossPriceTotal = sum(products, ProductSearchDto::grossPriceTotal);
        Map<Integer, BigDecimal> vatByRate = FinanceUtilities.groupAndSum(products, ProductSearchDto::getVat, ProductSearchDto::vatTotal);

        return new TotalSumsDto(netPriceTotal, vatTotal, grossPriceTotal, vatByRate);
    }
}
