package com.minhvu.feed.dto.mapper;

import com.minhvu.feed.dto.CommentDto;
import com.minhvu.feed.model.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toModel(CommentDto commentDto);

    CommentDto toDto(Comment comment);
}
