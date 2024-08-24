package com.minhvu.friend.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.friend.dto.AppUserDto;
import com.minhvu.friend.dto.FriendDto;
import com.minhvu.friend.dto.mapper.AppUserMapper;
import com.minhvu.friend.model.entities.AppUser;
import com.minhvu.friend.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class FriendProducer {

    private static final String TOPIC = "saveFriendTopic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void sendMessage(FriendDto friendDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(TOPIC, objectMapper.writeValueAsString(friendDto));

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
