package com.restaurantsystem.menumanagement.service;

import com.restaurantsystem.menumanagement.entity.MenuItem;
import com.restaurantsystem.menumanagement.web.dto.MenuItemDto;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {
    public MenuItemDto toDto(MenuItem menuItem) {
        return MenuItemDto.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .shortName(menuItem.getShortName())
                .description(menuItem.getDescription())
                .category(menuItem.getCategory())
                .vat(menuItem.getVat().getRate())
                .grossPrice(menuItem.getGrossPrice())
                .active(menuItem.isActive())
                .build();
    }
}
