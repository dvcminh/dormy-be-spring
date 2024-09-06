package com.minhvu.feed.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.feed.dto.AppUserDto;
import com.minhvu.feed.dto.SharedDto;
import com.minhvu.feed.dto.mapper.AppUserMapper;
import com.minhvu.feed.dto.mapper.SharedMapper;
import com.minhvu.feed.model.AppUser;
import com.minhvu.feed.model.Shared;
import com.minhvu.feed.repository.AppUserRepository;
import com.minhvu.feed.repository.SharedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShareConsumer {
    @Autowired
    SharedRepository sharedRepository;
    @Autowired
    SharedMapper sharedMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveShareTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SharedDto userDto = mapper.readValue(message, SharedDto.class);
        log.info("** Saving share to repository payload: '{}'", userDto.toString());
        Shared user = sharedMapper.toModel(userDto);
        sharedRepository.save(user);
        acknowledgment.acknowledge();
    }

}
