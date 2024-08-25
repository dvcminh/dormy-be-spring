package com.minhvu.interaction.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.SharedDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class SharedProducer {

    private static final String TOPIC = "saveSharedTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void send(SharedDto sharedDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(sharedDto));

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    System.err.println("Failed to send message: " + ex.getMessage());
                } else {
                    System.out.println("Message sent successfully: " + result.getRecordMetadata());
                }
            });
        } catch (JsonProcessingException exc) {
            throw new RuntimeException(exc);
        }
    }
}
