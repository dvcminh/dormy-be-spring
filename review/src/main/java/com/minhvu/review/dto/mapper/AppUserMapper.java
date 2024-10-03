package com.minhvu.review.dto.mapper;

import com.minhvu.review.dto.AppUserDto;
import com.minhvu.review.model.AppUser;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    AppUserDto toDto(AppUser user);
}
