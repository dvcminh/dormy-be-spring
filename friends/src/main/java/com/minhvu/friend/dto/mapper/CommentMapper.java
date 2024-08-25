package com.minhvu.friend.dto.mapper;

import com.minhvu.friend.dto.CommentDto;
import com.minhvu.friend.model.entities.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toModel(CommentDto commentDto);

    CommentDto toDto(Comment comment);
}
