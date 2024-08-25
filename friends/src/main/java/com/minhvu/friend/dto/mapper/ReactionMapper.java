package com.minhvu.friend.dto.mapper;

import com.minhvu.friend.dto.ReactionDto;
import com.minhvu.friend.model.entities.Reaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
    Reaction toModel(ReactionDto reactionDto);
    ReactionDto toDto(Reaction reaction);
}
