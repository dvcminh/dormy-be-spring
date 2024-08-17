package com.minhvu.interaction.dto.mapper;

import com.minhvu.interaction.dto.SharedDto;
import com.minhvu.interaction.entity.Shared;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SharedMapper {
    Shared toModel(SharedDto sharedDto);
    SharedDto toDto(Shared shared);
}
