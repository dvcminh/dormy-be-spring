package com.minhvu.review.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.review.dto.MediaEvent;
import com.minhvu.review.dto.PostEntityDto;
import com.minhvu.review.dto.PostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostProducer {

    private static final String TOPIC = "savePostTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void send(PostEntityDto postProducerDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(postProducerDto));

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
