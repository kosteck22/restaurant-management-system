package com.restaurantsystem.stockmanagement.service.kafka;

import com.restaurantsystem.common.messages.Message;
import com.restaurantsystem.common.messages.MessageSender;
import com.restaurantsystem.common.messages.event.NotEnoughProductsInStockEvent;
import com.restaurantsystem.common.messages.event.RecipeNotFoundEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockEventPublisherService {

    private final MessageSender eventPublisher;

    @Value("${topic.stock}")
    private String stockTopic;
    public void publishRecipeNotFoundEvent(RecipeNotFoundEvent event) {
        Message<RecipeNotFoundEvent> message = new Message<>(
                "RecipeNotFoundEvent",
                event
        );

        eventPublisher.send(message);
    }

    public void publishNotEnoughProductsInStock(NotEnoughProductsInStockEvent event) {
        Message<NotEnoughProductsInStockEvent> message = new Message<>(
                "NotEnoughProductsEvent",
                event
        );

        eventPublisher.send(message);
    }
}
