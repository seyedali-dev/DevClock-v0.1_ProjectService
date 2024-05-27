package com.seyed.ali.projectservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerEvent {

    private @Value("${kafka.topic.name}") String topicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object event) {
//        this.kafkaTemplate.send(this.topicName, event)
//                .whenComplete((message, exception) -> {
//                    if (exception != null)
//                        log.error("Error happened sending the message as event - Cause: {}", exception.getMessage());
//                    RecordMetadata recordMetadata = message.getRecordMetadata();
//                    log.info("topicName: {}", recordMetadata.topic());
//                    log.info("timestamp: {}", recordMetadata.timestamp());
//                    log.info("recordMetaData: {}", recordMetadata);
//                });
        // create message : org.springframework.messaging
        Message<Object> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, this.topicName)
                .build();

        // send the msg to the kafka topic
        this.kafkaTemplate.send(message)
                .whenComplete((msg, exception) -> {
                    if (exception != null)
                        log.error("Error happened sending the message as event - Cause: {}", exception.getMessage());
                    RecordMetadata recordMetadata = msg.getRecordMetadata();
                    log.info("topicName: {}", recordMetadata.topic());
                    log.info("timestamp: {}", recordMetadata.timestamp());
                    log.info("recordMetaData: {}", recordMetadata);
                });
    }

}
