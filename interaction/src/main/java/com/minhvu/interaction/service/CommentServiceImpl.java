package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.dto.CreateCommentRequest;
import com.minhvu.interaction.dto.UpdateCommentRequest;
import com.minhvu.interaction.dto.mapper.CommentMapper;
import com.minhvu.interaction.entity.Comment;
import com.minhvu.interaction.exception.NotFoundException;
import com.minhvu.interaction.kafka.CommentProducer;
import com.minhvu.interaction.repository.IcommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final IcommentRepository icommentRepository;
    private final CommentMapper commentMapper;
    private final CommentProducer commentProducer;
    private static final String COMMENT_NOT_FOUND = "Comment not found with this id : ";

    @Override
    public CommentDto save(UUID postId, CreateCommentRequest createCommentRequest)
    {
        Comment comment = Comment.builder()
                .postId(postId)
                .commentText(createCommentRequest.getCommentText())
                .build();
        CommentDto commentDto = commentMapper.toDto(comment);
        icommentRepository.save(comment);
        commentProducer.send(commentDto);
        return commentDto;
    }

    @Override
    public CommentDto update(UUID id, UpdateCommentRequest commentDto)
    {
        Comment comment = icommentRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + id));
        if(!comment.getUserId().equals(id)) {
            throw new NotFoundException("You are not allowed to update this comment");
        }
        comment.setCommentText(commentDto.getCommentText());
        Comment commentUpdated = icommentRepository.save(comment);
        return commentMapper.toDto(commentUpdated);
    }

    @Override
    public CommentDto getById(UUID id)
    {
        Comment comment = icommentRepository.findById(id).orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + id));
        return commentMapper.toDto(comment);
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

    @Override
    public List<CommentDto> getAll()
    {
        List<Comment> comments = icommentRepository.findAll();
        return comments
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public int getCountOfCommentsByPost(UUID postId)
    {
        List<Comment> comments = icommentRepository.findByPostId(postId);
        return comments.size();
    }

    @Override
    public void delete(UUID id, UUID uuid)
    {
        Comment comment = icommentRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + uuid));
        if(!comment.getUserId().equals(id)) {
            throw new NotFoundException("You are not allowed to delete this comment");
        }
        icommentRepository.delete(comment);
    }
}
