package com.minhvu.authservice.kafka;

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

    @Autowired
    private KafkaTemplate<String, AppUserDto> kafkaTemplate;

    @Autowired
    private KafkaTransactionManager<String, AppUserDto> transactionManager;

    @Transactional
    public void sendOrder(String userId, AppUserDto orderEvent) {
        CompletableFuture<SendResult<String, AppUserDto>> future = kafkaTemplate.send(TOPIC, userId, orderEvent);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                System.err.println("Failed to send message: " + ex.getMessage());
            } else {
                System.out.println("Message sent successfully: " + result.getRecordMetadata());
            }
        });
    }
}
