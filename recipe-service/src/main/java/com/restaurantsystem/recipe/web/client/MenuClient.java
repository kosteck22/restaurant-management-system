package com.restaurantsystem.recipe.web.client;

import com.restaurantsystem.recipe.web.dto.MenuItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "menu-management", url = "http://localhost:8103")
public interface MenuClient {
    @GetMapping("/api/v1/menu-items/{id}")
    public ResponseEntity<MenuItemDto> getById(@PathVariable("id") String id);
}
