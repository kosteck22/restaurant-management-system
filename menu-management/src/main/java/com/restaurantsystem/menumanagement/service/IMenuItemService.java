package com.restaurantsystem.menumanagement.service;

import com.restaurantsystem.menumanagement.web.dto.MenuItemDto;
import com.restaurantsystem.menumanagement.web.dto.MenuItemRequest;

import java.util.List;

public interface IMenuItemService {
    List<MenuItemDto> getActiveMenuItems();
    String save(MenuItemRequest articleRequest);
    MenuItemDto getById(String id);
    List<MenuItemDto> getActiveMenuItemsByIds(List<String> ids);
}
