package com.restaurantsystem.menumanagement.service;

import com.restaurantsystem.menumanagement.entity.MenuItem;
import com.restaurantsystem.menumanagement.entity.VatRate;
import com.restaurantsystem.menumanagement.exception.DuplicateResourceException;
import com.restaurantsystem.menumanagement.exception.ResourceNotFoundException;
import com.restaurantsystem.menumanagement.service.dao.MenuItemRepository;
import com.restaurantsystem.menumanagement.web.dto.MenuItemDto;
import com.restaurantsystem.menumanagement.web.dto.MenuItemRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService implements IMenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemService(MenuItemRepository articleRepository, MenuItemMapper articleMapper) {
        this.menuItemRepository = articleRepository;
        this.menuItemMapper = articleMapper;
    }

    @Override
    public List<MenuItemDto> getActiveMenuItems() {
        return menuItemRepository.findByActiveTrue().stream()
                .map(menuItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemDto getById(String id) {
        return menuItemRepository.findById(id)
                .map(menuItemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Article with id [%s] not found".formatted(id)));
    }

    @Override
    public String save(MenuItemRequest menuItemRequest) {
        throwIfNameAlreadyExist(menuItemRequest.name());
        throwIfShortNameAlreadyExist(menuItemRequest.shortName());
        VatRate vatRate = VatRate.valueOf(menuItemRequest.vat());

        MenuItem menuItem = MenuItem.builder()
                .name(menuItemRequest.name())
                .shortName(menuItemRequest.shortName())
                .category(menuItemRequest.category())
                .vat(vatRate)
                .grossPrice(menuItemRequest.grossPrice())
                .active(menuItemRequest.active())
                .build();

        return menuItemRepository.save(menuItem).getId();
    }

    private void throwIfShortNameAlreadyExist(String shortName) {
        menuItemRepository.findByShortName(shortName)
                .ifPresent(a -> {
                    throw new DuplicateResourceException("Menu item with shortName [%s] already exist".formatted(shortName));
                });
    }

    private void throwIfNameAlreadyExist(String name) {
        menuItemRepository.findByName(name)
                .ifPresent(a -> {
                    throw new DuplicateResourceException("Menu item with name [%s] already exist".formatted(name));
                });
    }
}
