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
    List<ReactionDto> getAll();
    void delete(UUID id, UUID uuid);
}
