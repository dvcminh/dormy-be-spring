package com.minhvu.feed.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${spring.kafka.consumer.user-group-id}")
    private String userGroupId;
    @Value(value = "${spring.kafka.consumer.friend-group-id}")
    private String friendGroupId;
    @Value(value = "${spring.kafka.consumer.post-group-id}")
    private String postGroupId;
    @Value(value = "${spring.kafka.consumer.comment-group-id}")
    private String commentGroupId;
    @Value(value = "${spring.kafka.consumer.reaction-group-id}")
    private String reactionGroupId;
    @Value(value = "${spring.kafka.consumer.shared-group-id}")
    private String shareGroupId;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, userGroupId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, friendGroupId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, postGroupId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, commentGroupId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, reactionGroupId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, shareGroupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
