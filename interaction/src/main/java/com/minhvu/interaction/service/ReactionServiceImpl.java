package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CreateReactionRequest;
import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.UpdateReactionRequest;
import com.minhvu.interaction.dto.mapper.ReactionMapper;
import com.minhvu.interaction.entity.Reaction;
import com.minhvu.interaction.exception.NotFoundException;
import com.minhvu.interaction.kafka.ReactionProducer;
import com.minhvu.interaction.repository.ReactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final ReactionMapper reactionMapper;
    private final ReactionProducer reactionProducer;
    private static final String REACTION_NOT_FOUND = "Reaction not found with this id : ";

    @Override
    public ReactionDto save(UUID userId, CreateReactionRequest createReactionRequest)
    {
        if(reactionRepository.existsByUserIdAndPostId(userId, createReactionRequest.getPostId())){
            throw new NotFoundException("You have already reacted to this post");
        }
        Reaction reaction = reactionRepository.saveAndFlush(Reaction.builder()
                .postId(createReactionRequest.getPostId())
                .reactionType(createReactionRequest.getReactionType())
                .userId(userId)
                .build());

        ReactionDto reactionDto = reactionMapper.toDto(reaction);
        reactionProducer.send(reactionDto);
        return reactionDto;
    }

    @Override
    public ReactionDto update(UUID id, UpdateReactionRequest reactionDto)
    {
        Reaction reaction = reactionRepository.findById(id).orElseThrow(() -> new NotFoundException(REACTION_NOT_FOUND + id));
        if (!reaction.getUserId().equals(id)) {
            throw new NotFoundException("You are not allowed to update this reaction");
        }
        reaction.setReactionType(reactionDto.getReactionType());
        Reaction reactionSaved = reactionRepository.save(reaction);
        return reactionMapper.toDto(reactionSaved);
    }

    @Override
    public ReactionDto getById(UUID id)
    {
        Reaction reaction = reactionRepository.findById(id).orElseThrow(() -> new NotFoundException(REACTION_NOT_FOUND + id));
        return reactionMapper.toDto(reaction);
    }



    @Override
    public List<ReactionDto> getAll()
    {
        List<Reaction> reactions = reactionRepository.findAll();
        return reactions
                .stream()
                .map(reactionMapper::toDto)
                .toList();
    }



    @Override
    public void delete(UUID id, UUID uuid)
    {
        if(reactionRepository.existsById(id)){
            if (!reactionRepository.findById(id).get().getUserId().equals(uuid)) {
                throw new NotFoundException("You are not allowed to delete this reaction");
            }
            reactionRepository.deleteById(id);
        }
        else {
            throw new NotFoundException(REACTION_NOT_FOUND + id);
        }
    }
}
