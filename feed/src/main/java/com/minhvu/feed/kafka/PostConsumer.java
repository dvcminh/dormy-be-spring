package com.minhvu.feed.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.feed.dto.PostEntityDto;
import com.minhvu.feed.dto.mapper.PostMapper;
import com.minhvu.feed.model.PostEntity;
import com.minhvu.feed.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostConsumer {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostMapper postMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "savePostTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PostEntityDto postEntity = mapper.readValue(message, PostEntityDto.class);
        log.info("** Saving user to repository payload: '{}'", postRepository.toString());
        postRepository.save(postMapper.toModel(postEntity));
    }
}
