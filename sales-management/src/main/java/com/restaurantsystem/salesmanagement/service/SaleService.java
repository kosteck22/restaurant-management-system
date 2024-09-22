package com.restaurantsystem.salesmanagement.service;

import com.restaurantsystem.common.messages.event.SaleCreatedEvent;
import com.restaurantsystem.salesmanagement.entity.Sale;
import com.restaurantsystem.salesmanagement.entity.SaleItem;
import com.restaurantsystem.salesmanagement.event.SaleEventPublisherService;
import com.restaurantsystem.salesmanagement.service.dao.SaleRepository;
import com.restaurantsystem.salesmanagement.web.client.MenuClient;
import com.restaurantsystem.salesmanagement.web.dto.MenuItemDto;
import com.restaurantsystem.salesmanagement.web.dto.SaleRequest;
import com.restaurantsystem.salesmanagement.web.dto.SaleItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService implements ISaleService {
    private final SaleRepository saleRepository;
    private final MenuClient menuClient;
    private final ISaleCreatorService saleCreatorService;
    private final SaleEventPublisherService eventPublisherService;


    @Override
    public String save(SaleRequest saleRequest) {
        List<MenuItemDto> menuItems = menuClient.getMenuItemsByIds(
                saleRequest.items()
                        .stream()
                        .map(SaleItemRequest::menuItemId)
                        .collect(Collectors.toList())
        ).getBody();

        Sale sale = saleCreatorService.create(saleRequest, menuItems);
        saleRepository.save(sale);

        SaleCreatedEvent event = new SaleCreatedEvent(
                sale.getId(),
                sale.getDate(),
                sale.getItems().stream()
                        .collect(Collectors
                                .toMap(
                                        SaleItem::getMenuItemId,
                                        SaleItem::getQuantity,
                                        Integer::sum))
        );

        eventPublisherService.publishSaleCreatedEvent(event);

        return sale.getId();
    }
}
