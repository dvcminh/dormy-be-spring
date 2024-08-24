package com.minhvu.friend.dto.mapper;

import com.minhvu.friend.dto.AppUserDto;
import com.minhvu.friend.model.entities.AppUser;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    AppUserDto toDto(AppUser user);
}
