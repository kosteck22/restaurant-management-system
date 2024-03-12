package com.restaurantsystem.menumanagement.web;

import com.restaurantsystem.menumanagement.service.IMenuItemService;
import com.restaurantsystem.menumanagement.web.dto.MenuItemDto;
import com.restaurantsystem.menumanagement.web.dto.MenuItemRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-items")
public class MenuItemController {
    private final IMenuItemService menuItemService;

    public MenuItemController(IMenuItemService articleService) {
        this.menuItemService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDto>> getMenuItems() {
        List<MenuItemDto> menuItems = menuItemService.getActiveMenuItems();
        if (menuItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok(menuItemService.getById(id));
    }

    @PostMapping("/list-by-ids")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByIds(@RequestBody List<String> ids) {
        List<MenuItemDto> menuItems = menuItemService.getActiveMenuItemsByIds(ids);

        return ResponseEntity.ok(menuItems);
    }

    @PostMapping
    public ResponseEntity<String> save(@Valid @RequestBody MenuItemRequest menuItemRequest) {
        String id = menuItemService.save(menuItemRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(id);
    }
}
