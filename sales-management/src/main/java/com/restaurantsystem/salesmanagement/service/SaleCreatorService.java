package com.restaurantsystem.salesmanagement.service;

import com.restaurantsystem.salesmanagement.entity.*;
import com.restaurantsystem.salesmanagement.exception.ResourceNotFoundException;
import com.restaurantsystem.salesmanagement.web.dto.MenuItemDto;
import com.restaurantsystem.salesmanagement.web.dto.SaleRequest;
import com.restaurantsystem.salesmanagement.web.dto.SoldItemRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SaleCreatorService implements ISaleCreatorService {
    @Override
    public Sale create(SaleRequest request, List<MenuItemDto> menuItems) {
        List<SoldItem> items = creteListOfItems(request.items(), menuItems);
        List<VatDetail> vatDetails = items.stream()
                .collect(Collectors.groupingBy(
                        SoldItem::getVatRate,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                SoldItem::getVatTotal,
                                BigDecimal::add)))
                .entrySet().stream()
                .map(entry -> new VatDetail(entry.getKey(), entry.getValue())).toList();

        return Sale.builder()
                .date(request.date())
                .items(items)
                .grossPrice(sum(items, SoldItem::getGrossPrice))
                .netPrice(sum(items, SoldItem::getNetPrice))
                .vatTotal(sum(items, SoldItem::getVatTotal))
                .vatDetails(vatDetails)
                .paymentDetails(request.paymentDetails().stream()
                        .map(p -> PaymentDetail.builder()
                                .paymentMethod(p.paymentMethod())
                                .amount(p.amount()).build())
                        .collect(Collectors.toList())
                )
                .build();
    }

    private List<SoldItem> creteListOfItems(List<SoldItemRequest> soldItemRequests, List<MenuItemDto> menuItems) {
        return soldItemRequests.stream()
                .map(s -> createItem(
                        s,
                        menuItems.stream()
                                .filter(m -> m.id().equals(s.articleId()))
                                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Menu item for id [%s] not found".formatted(s.articleId())))
                        )
                )
                .collect(Collectors.toList());
    }

    private SoldItem createItem(SoldItemRequest soldItemRequest, MenuItemDto menuItemDto) {
        FinancialOperations.ItemResult itemResult = FinancialOperations.calculateTotals(soldItemRequest.quantity(), menuItemDto.grossPrice(), menuItemDto.vat(), soldItemRequest.itemDiscount());

        return SoldItem.builder()
                .articleId(soldItemRequest.articleId())
                .shortName(menuItemDto.shortName())
                .quantity(soldItemRequest.quantity())
                .itemDiscount(soldItemRequest.itemDiscount())
                .grossPrice(itemResult.grossTotal)
                .netPrice(itemResult.netPrice)
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
