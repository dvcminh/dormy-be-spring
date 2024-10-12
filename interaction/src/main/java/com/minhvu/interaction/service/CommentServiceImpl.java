package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.dto.CreateCommentRequest;
import com.minhvu.interaction.dto.UpdateCommentRequest;
import com.minhvu.interaction.dto.mapper.CommentMapper;
import com.minhvu.interaction.entity.Comment;
import com.minhvu.interaction.exception.NotFoundException;
import com.minhvu.interaction.kafka.CommentProducer;
import com.minhvu.interaction.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CommentProducer commentProducer;
    private static final String COMMENT_NOT_FOUND = "Comment not found with this id : ";

    @Override
    public CommentDto save(UUID userId, CreateCommentRequest createCommentRequest)
    {
        Comment comment = commentRepository.saveAndFlush(Comment.builder()
                .postId(createCommentRequest.getPostId())
                .commentText(createCommentRequest.getCommentText())
                .userId(userId)
                .build());
        comment.setCreatedBy(userId);
        comment.setUpdatedBy(userId);
        CommentDto commentDto = commentMapper.toDto(comment);
        commentProducer.send(commentDto);
        return commentDto;
    }

    @Override
    public CommentDto update(UUID commentId, UUID userId, UpdateCommentRequest commentDto)
    {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + commentId));
        if(!comment.getUserId().equals(userId)) {
            throw new NotFoundException("You are not allowed to update this comment");
        }
        comment.setCommentText(commentDto.getCommentText());
        comment.setUpdatedBy(userId);
        Comment commentUpdated = commentRepository.saveAndFlush(comment);
        commentProducer.send(commentMapper.toDto(commentUpdated));
        return commentMapper.toDto(commentUpdated);
    }

    @Override
    public CommentDto getById(UUID id)
    {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + id));
        return commentMapper.toDto(comment);
    }



    @Override
    public List<CommentDto> getAll()
    {
        List<Comment> comments = commentRepository.findAll();
        return comments
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }



    @Override
    public void delete(UUID id, UUID uuid)
    {
        Comment comment = commentRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + uuid));
        if(!comment.getUserId().equals(id)) {
            throw new NotFoundException("You are not allowed to delete this comment");
        }
        comment.setDelete(true);
        commentProducer.send(commentMapper.toDto(commentRepository.saveAndFlush(comment)));
    }
}
