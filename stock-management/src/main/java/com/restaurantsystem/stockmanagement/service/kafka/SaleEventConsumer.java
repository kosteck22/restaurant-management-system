package com.restaurantsystem.stockmanagement.service.kafka;

import com.restaurantsystem.stockmanagement.service.IStockService;
import com.restaurantsystem.stockmanagement.service.SaleCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class StockEventConsumer {

    private final IStockService stockService;

    @KafkaListener(topics = "saleCreated", groupId = "saleGroup", containerFactory = "kafkaListenerContainerFactory")
    public void consumeSaleCreatedEvent(SaleCreatedEvent event) {
        log.info("Received message [{}] in group saleGroup", event.toString());
        stockService.consumeSaleCreatedEvent(event);
    }
}
