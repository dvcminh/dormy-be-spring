package com.minhvu.interaction.service;


import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.dto.CreateCommentRequest;
import com.minhvu.interaction.dto.UpdateCommentRequest;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    CommentDto save(UUID userId, CreateCommentRequest commentDto);
    CommentDto update(UUID commentId, UUID id, UpdateCommentRequest commentDto);
    CommentDto getById(UUID id);
    List<CommentDto> getAll();
    void delete(UUID id, UUID uuid);
}
