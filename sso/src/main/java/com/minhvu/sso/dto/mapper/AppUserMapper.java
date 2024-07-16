package com.minhvu.sso.dto.mapper;

import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.dto.response.UserProfileResponse;
import com.minhvu.sso.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    UserProfileResponse toUserProfile(AppUser user);

    AppUserDto toDto(AppUser user);

}
