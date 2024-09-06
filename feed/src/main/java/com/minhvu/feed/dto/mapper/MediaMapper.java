package com.minhvu.feed.dto.mapper;

import com.minhvu.feed.dto.MediaDto;
import com.minhvu.feed.model.Media;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MediaMapper {
    MediaDto toDto(Media friend);
    Media toModel(MediaDto friendDto);
}
