package com.restaurantsystem.common.messages.kafka;

import com.restaurantsystem.common.messages.MessageProducer;
import com.restaurantsystem.common.messages.InternalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public class KafkaSyncMessageProducer implements MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Override
    public void send(final String topic, final InternalEvent event) {
        try {
            final var sendResult = kafkaTemplate.send(topic, event);
            kafkaTemplate.flush();
            sendResult.get();
            log.info("Send message");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sending interrupted", e);
        } catch (KafkaException | ExecutionException e) {
            log.error("There was error while synchronous send event to Kafka cluster", e);
        }
    }
}
