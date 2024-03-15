package com.restaurantsystem.salesmanagement.service;

import com.restaurantsystem.salesmanagement.entity.Sale;
import com.restaurantsystem.salesmanagement.service.dao.SaleRepository;
import com.restaurantsystem.salesmanagement.web.client.MenuClient;
import com.restaurantsystem.salesmanagement.web.dto.MenuItemDto;
import com.restaurantsystem.salesmanagement.web.dto.SaleRequest;
import com.restaurantsystem.salesmanagement.web.dto.SoldItemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService implements ISaleService {
    private final SaleRepository saleRepository;
    private final MenuClient menuClient;
    private final ISaleCreatorService saleCreatorService;

    public SaleService(SaleRepository saleRepository, MenuClient menuClient, ISaleCreatorService saleCreatorService) {
        this.saleRepository = saleRepository;
        this.menuClient = menuClient;
        this.saleCreatorService = saleCreatorService;
    }

    @Override
    public String save(SaleRequest saleRequest) {
        List<MenuItemDto> menuItems = menuClient.getMenuItemsByIds(
                saleRequest.items()
                        .stream()
                        .map(SoldItemRequest::articleId)
                        .collect(Collectors.toList())
        ).getBody();

        Sale sale = saleCreatorService.create(saleRequest, menuItems);

        return saleRepository.save(sale).getId();
    }
}
