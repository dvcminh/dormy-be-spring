package com.minhvu.authservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.authservice.dto.AppUserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserProducer {

    private static final String TOPIC = "saveUserTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void sendOrder(AppUserDto appUserDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(appUserDto));

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    System.err.println("Failed to send message: " + ex.getMessage());
                } else {
                    System.out.println("Message sent successfully: " + result.getRecordMetadata());
                }
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
