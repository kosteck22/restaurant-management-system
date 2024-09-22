package com.restaurantsystem.salesmanagement.event;

import com.restaurantsystem.common.messages.Message;
import com.restaurantsystem.common.messages.MessageSender;
import com.restaurantsystem.common.messages.event.SaleCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleEventPublisherService {

    private final MessageSender eventPublisher;
    @Value("${sale.topic}")
    private String saleCreatedTopic;

    public void publishSaleCreatedEvent(SaleCreatedEvent event) {
        Message<SaleCreatedEvent> message = new Message<>(
            "SaleCreatedEvent",
                event
        );

        eventPublisher.send(message);
    }
}
