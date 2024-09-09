package com.minhvu.feed.service;

import com.minhvu.feed.dto.CommentDto;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    int getCountOfCommentsByPost(UUID postId);
    List<CommentDto> getAllCommentsByPostId(UUID postId);
}
