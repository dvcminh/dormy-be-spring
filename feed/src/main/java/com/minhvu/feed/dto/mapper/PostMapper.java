package com.minhvu.feed.dto.mapper;

import com.minhvu.feed.dto.PostEntityDto;
import com.minhvu.feed.model.PostEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostEntityDto toDto(PostEntity postEntity);
    PostEntity toModel(PostEntityDto postDto);
}
