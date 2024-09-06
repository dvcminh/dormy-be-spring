package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CreateReactionRequest;
import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.UpdateReactionRequest;
import com.minhvu.interaction.dto.mapper.ReactionMapper;
import com.minhvu.interaction.entity.Reaction;
import com.minhvu.interaction.entity.enums.ReactionType;
import com.minhvu.interaction.exception.NotFoundException;
import com.minhvu.interaction.kafka.ReactionProducer;
import com.minhvu.interaction.repository.IreactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final IreactionRepository ireactionRepository;
    private final ReactionMapper reactionMapper;
    private final ReactionProducer reactionProducer;
    private static final String REACTION_NOT_FOUND = "Reaction not found with this id : ";

    @Override
    public ReactionDto save(UUID userId, CreateReactionRequest createReactionRequest)
    {
        Reaction reaction = Reaction.builder()
                .postId(createReactionRequest.getPostId())
                .reactionType(createReactionRequest.getReactionType())
                .userId(userId)
                .build();
        ReactionDto reactionDto = reactionMapper.toDto(reaction);
        ireactionRepository.save(reaction);
        reactionProducer.send(reactionDto);
        return reactionDto;
    }

    @Override
    public ReactionDto update(UUID id, UpdateReactionRequest reactionDto)
    {
        Reaction reaction = ireactionRepository.findById(id).orElseThrow(() -> new NotFoundException(REACTION_NOT_FOUND + id));
        if (!reaction.getUserId().equals(id)) {
            throw new NotFoundException("You are not allowed to update this reaction");
        }
        reaction.setReactionType(reactionDto.getReactionType());
        Reaction reactionSaved = ireactionRepository.save(reaction);
        return reactionMapper.toDto(reactionSaved);
    }

    @Override
    public ReactionDto getById(UUID id)
    {
        Reaction reaction = ireactionRepository.findById(id).orElseThrow(() -> new NotFoundException(REACTION_NOT_FOUND + id));
        return reactionMapper.toDto(reaction);
    }

    @Override
    public List<ReactionDto> getAllReactionsByPostId(UUID postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return reactions
                .stream()
                .map(reactionMapper::toDto)
                .toList();
    }

    @Override
    public List<ReactionDto> getAll()
    {
        List<Reaction> reactions = ireactionRepository.findAll();
        return reactions
                .stream()
                .map(reactionMapper::toDto)
                .toList();
    }

    @Override
    public int getLikeCountOfPost(UUID postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.LIKE)
                .count();
    }

    @Override
    public int getLoveCountOfPost(UUID postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.LOVE)
                .count();
    }

    @Override
    public int getWowCountOfPost(UUID postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.WOW)
                .count();
    }

    @Override
    public int getHahahCountOfPost(UUID postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.HAHAH)
                .count();
    }

    @Override
    public int getSadCountOfPost(UUID postId)
    {

        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.SAD)
                .count();
    }

    @Override
    public int getAngryCountOfPost(UUID postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.ANGRY)
                .count();
    }

    @Override
    public int getCountReactionsOfPost(UUID postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return reactions.size();
    }

    @Override
    public void delete(UUID id, UUID uuid)
    {
        if(ireactionRepository.existsById(id)){
            if (!ireactionRepository.findById(id).get().getUserId().equals(uuid)) {
                throw new NotFoundException("You are not allowed to delete this reaction");
            }
            ireactionRepository.deleteById(id);
        }
        else {
            throw new NotFoundException(REACTION_NOT_FOUND + id);
        }
    }
}
