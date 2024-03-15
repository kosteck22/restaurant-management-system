package com.restaurantsystem.salesmanagement.web.client;

import com.restaurantsystem.salesmanagement.web.dto.MenuItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "menu-management", url = "http://localhost:8103")
public interface MenuClient {
    @PostMapping("/list-by-ids")
    ResponseEntity<List<MenuItemDto>> getMenuItemsByIds(@RequestBody List<String> ids);
}
