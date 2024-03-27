package com.restaurantsystem.stockmanagement.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleCreatedEvent {
    private String id;
    private LocalDateTime date;
    Map<String, Integer> soldItemsIdToQuantityMap;

}
