package com.minhvu.friend.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.friend.dto.AppUserDto;
import com.minhvu.friend.dto.CommentDto;
import com.minhvu.friend.dto.mapper.AppUserMapper;
import com.minhvu.friend.dto.mapper.CommentMapper;
import com.minhvu.friend.model.entities.AppUser;
import com.minhvu.friend.model.entities.Comment;
import com.minhvu.friend.repository.AppUserRepository;
import com.minhvu.friend.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
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

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveCommentTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CommentDto commentDto = mapper.readValue(message, CommentDto.class);
        log.info("** Saving user to repository payload: '{}'", commentDto.toString());
        Comment user = commentMapper.toModel(commentDto);
        commentRepository.save(user);
    }

}
