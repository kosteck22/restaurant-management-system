package com.restaurantsystem.common.messages.event;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotEnoughProductsInStockEvent {
    private String saleId;
}
