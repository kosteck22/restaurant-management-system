package com.restaurantsystem.stockmanagement.service.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(String message) {

    }
}
