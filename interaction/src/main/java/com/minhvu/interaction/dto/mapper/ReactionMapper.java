package com.minhvu.interaction.dto.mapper;

import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.entity.Reaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
    Reaction toModel(   ReactionDto reactionDto);
    ReactionDto toDto(Reaction reaction);
}
