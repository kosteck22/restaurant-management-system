package com.restaurantsystem.salesmanagement.service;

import com.restaurantsystem.salesmanagement.entity.*;
import com.restaurantsystem.salesmanagement.exception.ResourceNotFoundException;
import com.restaurantsystem.salesmanagement.exception.SaleValidationException;
import com.restaurantsystem.salesmanagement.web.dto.MenuItemDto;
import com.restaurantsystem.salesmanagement.web.dto.PaymentDetailRequest;
import com.restaurantsystem.salesmanagement.web.dto.SaleRequest;
import com.restaurantsystem.salesmanagement.web.dto.SaleItemRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SaleCreatorService implements ISaleCreatorService {
    @Override
    public Sale create(SaleRequest saleRequest, List<MenuItemDto> menuItems) {
        List<SaleItem> items = creteListOfSaleItems(saleRequest.items(), menuItems);
        List<VatDetail> vatDetails = createListOfVatDetails(items);
        BigDecimal grossPrice = sum(items, SaleItem::getGrossPriceTotal);
        BigDecimal netPrice = sum(items, SaleItem::getNetPriceTotal);
        BigDecimal vatTotal = sum(items, SaleItem::getVatTotal);
        BigDecimal paymentAmountTotal = saleRequest.paymentDetails().stream()
                .map(PaymentDetailRequest::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (grossPrice.compareTo(paymentAmountTotal) != 0) {
            throw new SaleValidationException("Amount paid is invalid [%s], it should be [%s].".formatted(paymentAmountTotal, grossPrice));
        }
        return Sale.builder()
                .date(saleRequest.date())
                .items(items)
                .grossPrice(grossPrice)
                .netPrice(netPrice)
                .vatTotal(vatTotal)
                .vatDetails(vatDetails)
                .paymentDetails(saleRequest.paymentDetails().stream()
                        .map(p -> PaymentDetail.builder()
                                .paymentMethod(p.paymentMethod())
                                .amount(p.amount()).build())
                        .collect(Collectors.toList())
                )
                .stockStatus(StockStatus.PENDING)
                .build();
    }

    private static List<VatDetail> createListOfVatDetails(List<SaleItem> items) {
        return items.stream()
                .collect(Collectors.groupingBy(
                        SaleItem::getVatRate,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                SaleItem::getVatTotal,
                                BigDecimal::add)))
                .entrySet().stream()
                .map(entry -> new VatDetail(entry.getKey(), entry.getValue())).toList();
    }

    private List<SaleItem> creteListOfSaleItems(List<SaleItemRequest> saleItemsRequest, List<MenuItemDto> menuItems) {
        return saleItemsRequest.stream()
                .map(si -> createSaleItem(
                                si,
                                menuItems.stream()
                                        .filter(m -> m.id().equals(si.menuItemId()))
                                        .findFirst()
                                        .orElseThrow(() -> new ResourceNotFoundException("Menu item with id [%si] not available".formatted(si.menuItemId())))
                        )
                )
                .collect(Collectors.toList());
    }

    private SaleItem createSaleItem(SaleItemRequest saleItemRequest, MenuItemDto menuItemDto) {
        FinancialOperations.ItemResult itemResult = FinancialOperations.calculateTotals(saleItemRequest.quantity(), menuItemDto.grossPrice(), menuItemDto.vat(), saleItemRequest.discount());

        return SaleItem.builder()
                .menuItemId(menuItemDto.id())
                .shortName(menuItemDto.shortName())
                .quantity(saleItemRequest.quantity())
                .itemDiscount(saleItemRequest.discount())
                .grossPrice(menuItemDto.grossPrice())
                .grossPriceTotal(itemResult.grossTotal)
                .netPriceTotal(itemResult.netPriceTotal)
                .vatRate(VatRate.valueOf(menuItemDto.vat()))
                .vatTotal(itemResult.vatTotal)
                .build();
    }

    private <T> BigDecimal sum(List<T> list, Function<T, BigDecimal> valueExtractor) {
        return list.stream()
                .map(valueExtractor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
