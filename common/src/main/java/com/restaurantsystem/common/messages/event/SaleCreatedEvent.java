package com.restaurantsystem.common.messages.event;

import com.restaurantsystem.common.messages.InternalEvent;
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
public class SaleCreatedEvent implements InternalEvent {
    private String saleId;
    private LocalDateTime date;
    Map<String, Integer> soldItemsIdToQuantityMap;

}
