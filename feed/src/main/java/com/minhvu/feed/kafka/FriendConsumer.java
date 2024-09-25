package com.minhvu.feed.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.feed.dto.FriendDto;
import com.minhvu.feed.dto.mapper.FriendMapper;
import com.minhvu.feed.model.Friend;
import com.minhvu.feed.repository.FriendRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FriendConsumer {
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    FriendMapper friendMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveFriendTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FriendDto userDto = mapper.readValue(message, FriendDto.class);
        log.info("** Saving friend to repository payload: '{}'", userDto.toString());
        Friend user = friendMapper.toModel(userDto);
        friendRepository.save(user);
    }
}
