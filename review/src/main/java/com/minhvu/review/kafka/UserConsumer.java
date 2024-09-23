package com.minhvu.review.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.review.dto.inter.AppUserDto;
import com.minhvu.review.dto.mapper.AppUserMapper;
import com.minhvu.review.model.AppUser;
import com.minhvu.review.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserConsumer {
    @Autowired
    AppUserRepository userRepository;
    @Autowired
    AppUserMapper userMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveUserTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AppUserDto userDto = mapper.readValue(message, AppUserDto.class);
        log.info("** Saving user to repository payload: '{}'", userDto.toString());
        AppUser user = userMapper.toModel(userDto);
        userRepository.save(user);
    }

}
