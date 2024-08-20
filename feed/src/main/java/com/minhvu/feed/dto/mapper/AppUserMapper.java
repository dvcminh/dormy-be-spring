package com.minhvu.feed.dto.mapper;

import com.minhvu.feed.dto.AppUserDto;
import com.minhvu.feed.model.AppUser;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    AppUserDto toDto(AppUser user);
}
