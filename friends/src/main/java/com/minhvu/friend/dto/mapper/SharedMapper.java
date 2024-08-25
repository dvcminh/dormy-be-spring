package com.minhvu.friend.dto.mapper;

import com.minhvu.friend.dto.SharedDto;
import com.minhvu.friend.model.entities.Shared;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SharedMapper {
    Shared toModel(SharedDto sharedDto);
    SharedDto toDto(Shared shared);
}
