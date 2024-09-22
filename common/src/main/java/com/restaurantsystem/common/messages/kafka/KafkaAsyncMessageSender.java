package com.restaurantsystem.common.messages.kafka;

import com.restaurantsystem.common.messages.MessageProducer;
import com.restaurantsystem.common.messages.InternalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;


@Slf4j
@RequiredArgsConstructor
public class KafkaAsyncMessageProducer implements MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void send(String topic, InternalEvent event) {
        try {
            kafkaTemplate.send(topic, event);
            log.info("send message");
        } catch (KafkaException e) {
            log.error("There was error while asynchronous send event to Kafka cluster", e);
        }
    }
}
