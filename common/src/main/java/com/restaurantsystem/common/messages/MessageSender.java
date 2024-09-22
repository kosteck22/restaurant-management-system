package com.restaurantsystem.common.messages;

public interface MessageProducer {
    void send(Message<?> m);
}
