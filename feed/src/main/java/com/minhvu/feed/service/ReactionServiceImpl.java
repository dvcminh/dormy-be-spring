package com.minhvu.feed.service;

import com.minhvu.feed.dto.ReactionDto;
import com.minhvu.feed.dto.mapper.ReactionMapper;
import com.minhvu.feed.model.Reaction;
import com.minhvu.feed.model.ReactionType;
import com.minhvu.feed.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository ireactionRepository;
    private final ReactionMapper reactionMapper;
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
}
