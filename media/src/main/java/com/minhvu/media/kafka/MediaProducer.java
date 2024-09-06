package com.minhvu.media.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.media.dto.FeedEvent;
import com.minhvu.media.dto.MediaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaProducer {

    private static final String FEED_TOPIC = "saveImageTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendFeedEvent(MediaDto mediaDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String feedEventJson = objectMapper.writeValueAsString(mediaDto);
            kafkaTemplate.send(FEED_TOPIC, feedEventJson);
        } catch (JsonProcessingException exc) {
            throw new RuntimeException(exc);
        }
    }
}