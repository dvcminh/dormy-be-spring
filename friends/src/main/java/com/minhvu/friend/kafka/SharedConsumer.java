package com.minhvu.friend.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.friend.dto.AppUserDto;
import com.minhvu.friend.dto.SharedDto;
import com.minhvu.friend.dto.mapper.SharedMapper;
import com.minhvu.friend.model.entities.AppUser;
import com.minhvu.friend.model.entities.Shared;
import com.minhvu.friend.repository.SharedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SharedConsumer {
    @Autowired
    SharedRepository sharedRepository;
    @Autowired
    SharedMapper sharedMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveSharedTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SharedDto userDto = mapper.readValue(message, SharedDto.class);
        log.info("** Saving user to repository payload: '{}'", userDto.toString());
        Shared user = sharedMapper.toModel(userDto);
        sharedRepository.save(user);
    }

}
