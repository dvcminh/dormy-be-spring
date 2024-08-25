package com.minhvu.interaction.service;


import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.dto.CreateCommentRequest;
import com.minhvu.interaction.dto.UpdateCommentRequest;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    CommentDto save(UUID postId, CreateCommentRequest commentDto);
    CommentDto update(UUID id, UpdateCommentRequest commentDto);
    CommentDto getById(UUID id);
    List<CommentDto> getAllCommentsByPostId(UUID postId);
    List<CommentDto> getAll();
    int getCountOfCommentsByPost(UUID postId);
    void delete(UUID id, UUID uuid);
}
