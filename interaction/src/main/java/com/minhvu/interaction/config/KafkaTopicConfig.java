package com.minhvu.interaction.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic inputTopic1() {
        return new NewTopic("saveCommentTopic", 1, (short) 1);
    }
    @Bean
    public NewTopic inputTopic2() {
        return new NewTopic("saveReactionTopic", 1, (short) 1);
    }
    @Bean
    public NewTopic inputTopic3() {
        return new NewTopic("saveSharedTopic", 1, (short) 1);
    }
}
