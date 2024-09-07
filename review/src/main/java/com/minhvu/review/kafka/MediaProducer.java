package com.minhvu.review.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.review.dto.MediaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaProducer {

    private static final String MEDIA_TOPIC = "uploadMediaTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMediaEvent(MediaEvent mediaEvent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(MEDIA_TOPIC, objectMapper.writeValueAsString(mediaEvent));
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send message: {}", ex.getMessage());
                } else {
                    log.info("Message sent successfully: {}", result.getRecordMetadata());
                }
            });
        } catch (JsonProcessingException exc) {
            throw new RuntimeException(exc);
        }
    }
}