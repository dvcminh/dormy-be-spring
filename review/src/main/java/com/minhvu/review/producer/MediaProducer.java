package com.minhvu.review.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.review.dto.MediaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaProducer {

    private static final String MEDIA_TOPIC = "uploadMediaTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMediaEvent(MediaEvent mediaEvent) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String mediaEventJson = objectMapper.writeValueAsString(mediaEvent);
            kafkaTemplate.send(MEDIA_TOPIC, mediaEventJson);
        } catch (JsonProcessingException exc) {
            throw new RuntimeException(exc);
        }
    }
}