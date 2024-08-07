package com.minhvu.interaction.service;


import com.minhvu.interaction.dto.CommentDto;

import java.util.List;

public interface IcommentService {

    CommentDto save(Long postId,CommentDto commentDto);
    CommentDto update(Long id, CommentDto commentDto);
    CommentDto getById(Long id);
    List<CommentDto> getAllCommentsByPostId(Long postId);
    List<CommentDto> getAll();
    int getCountOfCommentsByPost(Long postId);
    void delete(Long id);
}
