package com.minhvu.notification.dto.mapper;

import com.minhvu.notification.dto.model.AppUserDto;
import com.minhvu.notification.model.AppUser;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    AppUserDto toDto(AppUser user);
}
