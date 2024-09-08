package com.minhvu.feed.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.feed.dto.AppUserDto;
import com.minhvu.feed.dto.CommentDto;
import com.minhvu.feed.dto.mapper.AppUserMapper;
import com.minhvu.feed.dto.mapper.CommentMapper;
import com.minhvu.feed.model.AppUser;
import com.minhvu.feed.model.Comment;
import com.minhvu.feed.repository.AppUserRepository;
import com.minhvu.feed.repository.CommentRepository;
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
public class CommentConsumer {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentMapper commentMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "CommentTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CommentDto userDto = mapper.readValue(message, CommentDto.class);
        log.info("** Saving comment to repository payload: '{}'", userDto.toString());
        Comment user = commentMapper.toModel(userDto);
        commentRepository.save(user);
        acknowledgment.acknowledge();
    }

}