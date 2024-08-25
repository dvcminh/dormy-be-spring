package com.minhvu.interaction.dto.mapper;

import com.minhvu.interaction.dto.AppUserDto;
import com.minhvu.interaction.entity.AppUser;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    AppUserDto toDto(AppUser user);
}
