package com.minhvu.interaction.service.Impl;

import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.mapper.ReactionMapper;
import com.minhvu.interaction.entity.Reaction;
import com.minhvu.interaction.entity.enums.ReactionType;
import com.minhvu.interaction.exception.NotFoundException;
import com.minhvu.interaction.repository.IreactionRepository;
import com.minhvu.interaction.service.IreactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReactionService implements IreactionService {

    private final IreactionRepository ireactionRepository;
    private final ReactionMapper reactionMapper;
    private static final String REACTION_NOT_FOUND = "Reaction not found with this id : ";

    @Override
    public ReactionDto save(Long postId, ReactionDto reactionDto)
    {
        reactionDto.setCreatedAt(LocalDateTime.now());
        reactionDto.setPostId(postId);
        Reaction reaction = ireactionRepository.save(reactionMapper.toModel(reactionDto));
        return reactionMapper.toDto(reaction);
    }

    @Override
    public ReactionDto update(Long id, ReactionDto reactionDto)
    {
        Reaction reaction = ireactionRepository.findById(id).orElseThrow(() -> new NotFoundException(REACTION_NOT_FOUND + id));
        reaction.setReactionType(reactionDto.getReactionType());
        Reaction reactionSaved = ireactionRepository.save(reaction);
        return reactionMapper.toDto(reactionSaved);
    }

    @Override
    public ReactionDto getById(Long id)
    {
        Reaction reaction = ireactionRepository.findById(id).orElseThrow(() -> new NotFoundException(REACTION_NOT_FOUND + id));
        return reactionMapper.toDto(reaction);
    }

    @Override
    public List<ReactionDto> getAllReactionsByPostId(Long postId)
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
    public int getLikeCountOfPost(Long postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.LIKE)
                .count();
    }

    @Override
    public int getLoveCountOfPost(Long postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.LOVE)
                .count();
    }

    @Override
    public int getWowCountOfPost(Long postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.WOW)
                .count();
    }

    @Override
    public int getHahahCountOfPost(Long postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.HAHAH)
                .count();
    }

    @Override
    public int getSadCountOfPost(Long postId)
    {

        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.SAD)
                .count();
    }

    @Override
    public int getAngryCountOfPost(Long postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return (int) reactions.stream()
                .filter(reaction -> reaction.getReactionType() == ReactionType.ANGRY)
                .count();
    }

    @Override
    public int getCountReactionsOfPost(Long postId)
    {
        List<Reaction> reactions = ireactionRepository.findByPostId(postId);
        return reactions.size();
    }

    @Override
    public void delete(Long id)
    {
        if(ireactionRepository.existsById(id)){
            ireactionRepository.deleteById(id);
        }
        else {
            throw new NotFoundException(REACTION_NOT_FOUND + id);
        }
    }
}
