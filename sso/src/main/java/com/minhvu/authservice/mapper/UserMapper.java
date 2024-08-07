package com.minhvu.authservice.mapper;
import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.UserDto;
import com.minhvu.authservice.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper()
public interface UserMapper {
    AppUser toModel(AppUserDto userDto);
    AppUserDto toDto(AppUser user);
}
