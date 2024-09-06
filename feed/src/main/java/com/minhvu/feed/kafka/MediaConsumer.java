package com.minhvu.feed.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.feed.dto.FeedEvent;
import com.minhvu.feed.dto.MediaDto;
import com.minhvu.feed.dto.mapper.MediaMapper;
import com.minhvu.feed.repository.MediaRepository;
import com.minhvu.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MediaConsumer {
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;

    @KafkaListener(topics = "saveImageTopic", groupId = "feedGroup")
    public void consume(String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MediaDto mediaDto = objectMapper.readValue(message, MediaDto.class);
        log.info("Consumed message: {}", mediaDto);
        mediaRepository.save(mediaMapper.toModel(mediaDto));
    }
}