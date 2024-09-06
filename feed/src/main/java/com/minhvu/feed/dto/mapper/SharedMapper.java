package com.minhvu.feed.dto.mapper;

import com.minhvu.feed.dto.SharedDto;
import com.minhvu.feed.model.Shared;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SharedMapper {
    Shared toModel(SharedDto sharedDto);
    SharedDto toDto(Shared shared);
}
