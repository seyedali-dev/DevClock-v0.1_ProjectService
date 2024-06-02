package com.seyed.ali.projectservice.config.event;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    private @Value("${spring.kafka.topic.name}") String topicName;

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(this.topicName)
                .partitions(3)
                .replicas(1)
                .build();
    }

}
