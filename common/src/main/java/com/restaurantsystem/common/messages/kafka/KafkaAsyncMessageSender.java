package com.restaurantsystem.common.messages.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantsystem.common.messages.Message;
import com.restaurantsystem.common.messages.MessageSender;
import com.restaurantsystem.common.messages.InternalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;


@Slf4j
@RequiredArgsConstructor
public class KafkaAsyncMessageSender implements MessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    @Override
    public void send(Message<?> m) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(m);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, jsonMessage);
            record.headers().add("type", m.getType().getBytes());

            kafkaTemplate.send(record);
            log.info("send message");
        } catch (KafkaException e) {
            log.error("There was error while asynchronous send event to Kafka cluster", e);
        } catch (Exception e) {
            throw new RuntimeException("Could not transform and send message: "+ e.getMessage(), e);
        }
    }
}
