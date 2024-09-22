package com.restaurantsystem.stockmanagement.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantsystem.common.messages.Message;
import com.restaurantsystem.common.messages.event.SaleCreatedEvent;
import com.restaurantsystem.stockmanagement.service.IStockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class SaleEventConsumer {

    private final IStockService stockService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "saleTopic", containerFactory = "kafkaListenerContainerFactory")
    public void consumeSaleCreatedEvent(String messagePayloadJson, @Header("type") String messageType) throws JsonProcessingException {
        if (!"SaleCreatedEvent".equals(messageType)) {
            return;
        }

        log.info("Received message [{}] in group saleGroup", messagePayloadJson);
        Message<SaleCreatedEvent> message = objectMapper.readValue(messagePayloadJson, new TypeReference<>() {});
        SaleCreatedEvent saleCreatedEvent = message.getData();

        stockService.consumeSaleCreatedEvent(saleCreatedEvent);
    }
}
