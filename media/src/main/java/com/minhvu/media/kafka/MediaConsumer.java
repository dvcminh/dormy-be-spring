package com.minhvu.media.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.media.dto.MediaEvent;
import com.minhvu.media.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaConsumer {

    private final ImageService mediaService;

    @KafkaListener(topics = "uploadMediaTopic", groupId = "mediaGroup")
    public void consume(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MediaEvent mediaEvent = objectMapper.readValue(message, MediaEvent.class);
        log.info("Consumed message: {}", mediaEvent);
        mediaEvent.getFiles().forEach(file -> mediaService.upload(file, mediaEvent.getUserId(), mediaEvent.getPostId()));
    }
}