package com.minhvu.review.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.review.dto.request.Notification;
import com.minhvu.review.dto.request.NotificationUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Component
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotificationMessage(Notification notification) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("saveNotificationTopic", mapper.writeValueAsString(notification));

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

//    public void sendNotificationUserMessage(NotificationUser notificationUser) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            kafkaTemplate.send("saveNotificationUserTopic", mapper.writeValueAsString(notificationUser))
//                    .addCallback(
//                            result -> log.info("Message sent to topic: {}", notificationUser.toString()),
//                            ex -> log.error("Failed to send message", ex)
//                    );
//        } catch (JsonProcessingException exc) {
//            throw new RuntimeException(exc);
//        }
//    }
}
