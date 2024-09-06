package com.minhvu.media.dto.mapper;

import com.minhvu.media.dto.MediaDto;
import com.minhvu.media.model.Media;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MediaMapper {
    MediaDto toDto(Media friend);
    Media toModel(MediaDto friendDto);
}