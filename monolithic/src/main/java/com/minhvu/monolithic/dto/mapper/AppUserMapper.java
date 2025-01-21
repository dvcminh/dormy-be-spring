package com.minhvu.monolithic.dto.mapper;


import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.entity.AppUser;
import org.mapstruct.Mapper;


@Mapper()
public interface AppUserMapper {

    AppUserDto toDto(AppUser user);

}
