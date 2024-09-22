package com.restaurantsystem.common.messages;

public interface MessageSender {
    void send(Message<?> m);
}
