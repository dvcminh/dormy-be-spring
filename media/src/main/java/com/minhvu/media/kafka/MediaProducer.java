package com.minhvu.media.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.media.dto.FeedEvent;
import com.minhvu.media.dto.MediaDto;
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

    private static final String FEED_TOPIC = "saveMediaTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendFeedEvent(MediaDto mediaDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(FEED_TOPIC, objectMapper.writeValueAsString(mediaDto));
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