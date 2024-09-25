package com.minhvu.feed.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.feed.dto.ReactionDto;
import com.minhvu.feed.dto.mapper.ReactionMapper;
import com.minhvu.feed.model.Reaction;
import com.minhvu.feed.repository.ReactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReactionConsumer {
    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    ReactionMapper reactionMapper;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @KafkaListener(topicPartitions = {@TopicPartition(topic = "saveReactionTopic",
            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
    public void receive(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ReactionDto userDto = mapper.readValue(message, ReactionDto.class);
        log.info("** Saving reaction to repository payload: '{}'", userDto.toString());
        Reaction reaction = reactionMapper.toModel(userDto);
        reactionRepository.save(reaction);
    }

}
