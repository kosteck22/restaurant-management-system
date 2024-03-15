package com.restaurantsystem.salesmanagement.service;

import com.restaurantsystem.salesmanagement.entity.Sale;
import com.restaurantsystem.salesmanagement.web.dto.MenuItemDto;
import com.restaurantsystem.salesmanagement.web.dto.SaleRequest;

import java.util.List;

public interface ISaleCreatorService {
    Sale create(SaleRequest request, List<MenuItemDto> menuItems);
}
