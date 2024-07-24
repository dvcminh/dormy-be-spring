package com.minhvu.notification.dto.mapper;

import com.minhvu.notification.dto.model.AppUserDto;
import com.minhvu.notification.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Mapper(componentModel = "spring")
public interface AppUserMapper {

    AppUser toModel(AppUserDto userDto);

    AppUserDto toDto(AppUser user);

    @Named("getAvatarUrl")
    public static String getAvatarUrl(UUID id) {
        return "https://erp.innovation.com.vn/api/noauth/user/" + id + "/avatar";
    }
}
