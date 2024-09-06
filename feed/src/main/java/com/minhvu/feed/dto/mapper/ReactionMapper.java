package com.minhvu.feed.dto.mapper;

import com.minhvu.feed.dto.ReactionDto;
import com.minhvu.feed.model.Reaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
    Reaction toModel(ReactionDto reactionDto);
    ReactionDto toDto(Reaction reaction);
}
