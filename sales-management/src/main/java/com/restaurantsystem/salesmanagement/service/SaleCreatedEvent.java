package com.restaurantsystem.salesmanagement.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class SaleCreatedEvent {
    private String id;
    private LocalDateTime date;
    Map<String, Integer> soldItemsIdToQuantityMap;

}
