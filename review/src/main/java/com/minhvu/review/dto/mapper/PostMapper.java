package com.minhvu.review.dto.mapper;

import com.minhvu.review.dto.PostEntityDto;
import com.minhvu.review.model.PostEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostEntityDto toDto(PostEntity postEntity);
    PostEntity toModel(PostEntityDto postDto);
}
