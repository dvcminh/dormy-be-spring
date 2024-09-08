package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CreateReactionRequest;
import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.UpdateReactionRequest;

import java.util.List;
import java.util.UUID;

public interface ReactionService {

    ReactionDto save(UUID userId, CreateReactionRequest createReactionRequest);
    ReactionDto update(UUID id, UpdateReactionRequest reactionDto);
    ReactionDto getById(UUID id);
    List<ReactionDto> getAllReactionsByPostId(UUID postId);
    List<ReactionDto> getAll();
    int getLikeCountOfPost(UUID postId);
    int getLoveCountOfPost(UUID postId);
    int getWowCountOfPost(UUID postId);
    int getHahahCountOfPost(UUID postId);
    int getSadCountOfPost(UUID postId);
    int getAngryCountOfPost(UUID postId);

    int getCountReactionsOfPost(UUID postId);
    void delete(UUID id, UUID uuid);
}