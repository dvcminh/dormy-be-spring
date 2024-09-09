package com.minhvu.feed.service;

import com.minhvu.feed.dto.CommentDto;
import com.minhvu.feed.dto.mapper.CommentMapper;
import com.minhvu.feed.model.Comment;
import com.minhvu.feed.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository icommentRepository;
    private final CommentMapper commentMapper;

    @Override
    public int getCountOfCommentsByPost(UUID postId)
    {
        List<Comment> comments = icommentRepository.findByPostId(postId);
        return comments.size();
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(UUID postId)
    {
        List<Comment> comments = icommentRepository.findByPostId(postId);
        return comments
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }
}
