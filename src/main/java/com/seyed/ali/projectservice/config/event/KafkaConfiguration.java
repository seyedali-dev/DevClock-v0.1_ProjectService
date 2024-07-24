package com.seyed.ali.projectservice.config.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Configuration Explanation
 * <p>
 * Issue:
 * Despite setting the bootstrap servers to a specific address in the docker-compose file
 * (e.g., {@code kafka-kraft:9094}), the Kafka {@link AdminClient} was attempting to connect
 * to {@code localhost:9092}. This caused connection failures when running in a Docker
 * environment where {@code localhost} doesn't refer to the running Kafka container.
 * <p>
 * Root Cause:
 * Spring Kafka's auto-configuration was applying default settings that weren't being
 * properly overridden by our custom configuration. The {@link AdminClient} was using
 * these default settings instead of our specified bootstrap servers.
 * <p>
 * Resolution:
 * We explicitly configured both {@link AdminClient} and {@link KafkaAdmin} beans with
 * the correct bootstrap servers. The {@link KafkaAdmin} bean, in particular, ensures
 * that all Kafka-related auto-configuration uses the correct settings. This guarantees
 * that all Kafka operations, including topic management and metadata retrieval, use
 * the correct Kafka broker address.
 * <p>
 * Key Points:
 * <ol>
 *     <li>Always explicitly configure Kafka clients in containerized environments.</li>
 *     <li>Be aware of Spring Boot's auto-configuration and when to override it.</li>
 *     <li>Use environment-specific configuration (like Docker container names) for services.</li>
 *     <li>Logging configuration values helps in debugging these issues.</li>
 *     <li>{@link KafkaAdmin} is crucial for ensuring all Kafka-related auto-configuration uses the correct settings.</li>
 *     <li>Using a single environment variable for host and port ({@code KAFKA_SERVER_HOST_AND_PORT}).</li>
 * </ol>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    private @Value("${spring.kafka.topic.name}") String topicName;
    private @Value("${spring.kafka.producer.bootstrap-servers}") String bootstrapServers;

    private final ConfigurableEnvironment env;

    @PostConstruct
    public void logAllKafkaProperties() {
        log.info("---> kafka related log: Kafka bootstrap servers from @Value");
        log.info("---> kafka related log: KAFKA_SERVER_HOST_AND_PORT env variable: {}", System.getenv("KAFKA_SERVER_HOST_AND_PORT"));
        env.getPropertySources()
                .forEach(propertySource -> {
                    if (propertySource instanceof EnumerablePropertySource) {
                        Arrays.stream(((EnumerablePropertySource<?>) propertySource).getPropertyNames())
                                .filter(prop -> prop.toLowerCase().contains("kafka"))
                                .forEach(prop -> log.info("---> kafka related log: {} = {}", prop, env.getProperty(prop)));
                    }
                });
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(this.topicName)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public AdminClient adminClient() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        return AdminClient.create(configs);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);

        KafkaAdmin admin = new KafkaAdmin(configs);
        admin.setFatalIfBrokerNotAvailable(true);
        return admin;
    }

}
