package com.minhvu.review.dto.mapper;

import com.minhvu.review.dto.model.AppUserDto;
import com.minhvu.review.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    AppUserDto toDto(AppUser user);


}
