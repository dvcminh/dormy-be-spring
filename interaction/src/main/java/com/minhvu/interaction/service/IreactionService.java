package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.dto.ReactionDto;

import java.util.List;

public interface IreactionService {

    ReactionDto save(Long postId, ReactionDto reactionDto);
    ReactionDto update(Long id, ReactionDto reactionDto);
    ReactionDto getById(Long id);
    List<ReactionDto> getAllReactionsByPostId(Long postId);
    List<ReactionDto> getAll();
    int getLikeCountOfPost(Long postId);
    int getLoveCountOfPost(Long postId);
    int getWowCountOfPost(Long postId);
    int getHahahCountOfPost(Long postId);
    int getSadCountOfPost(Long postId);
    int getAngryCountOfPost(Long postId);

    int getCountReactionsOfPost(Long postId);
    void delete(Long id);
}
