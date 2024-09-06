package com.minhvu.interaction.dto.mapper;

import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toModel(CommentDto commentDto);

    CommentDto toDto(Comment comment);
}
