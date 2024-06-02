package com.seyed.ali.projectservice.event;

import com.seyed.ali.projectservice.model.enums.OperationType;
import com.seyed.ali.projectservice.model.payload.ProjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectEventProducer {

    private final NewTopic topic;
    private final KafkaTemplate<String, ProjectDTO> kafkaTemplate;

    public void sendMessage(ProjectDTO projectDTO, OperationType operationType) {
        try {
            Message<ProjectDTO> message = MessageBuilder
                    .withPayload(projectDTO)
                    .setHeader(KafkaHeaders.TOPIC, this.topic.name())
                    .setHeader("OperationType", operationType.name())
                    .build();

            this.kafkaTemplate.send(message)
                    .whenComplete((msg, exc) -> {
                        if (exc == null) log.info("message Send! key: {}", msg.getRecordMetadata().topic());
                        else log.error("exception occurred! - Cause: {}", exc.getMessage());
                    });
        } catch (Exception e) {
            log.error("Exception in catch: {}", e.getMessage());
        }
    }

}
