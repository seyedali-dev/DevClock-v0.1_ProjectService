package com.seyed.ali.projectservice.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seyed.ali.projectservice.model.domain.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventProducer {

    private @Value("${kafka.topic.name}") String topicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(Project project) {
        // create a project_event object
        ProjectEvent projectEvent = ProjectEvent.builder()
                .message("Sending projectID: " + project.getProjectId() + " as event")
                .event(project)
                .build();

        // convert project_event into Map<String, Object>
        Map<String, Object> projectMapEvent = this.objectMapper.convertValue(projectEvent, new TypeReference<>() {
        });

        // create message : org.springframework.messaging
        Message<Map<String, Object>> event = MessageBuilder
                .withPayload(projectMapEvent)
                .setHeader(KafkaHeaders.TOPIC, this.topicName)
                .build();

        // send the msg to the kafka topic
        this.kafkaTemplate.send(this.topicName, projectMapEvent)
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
