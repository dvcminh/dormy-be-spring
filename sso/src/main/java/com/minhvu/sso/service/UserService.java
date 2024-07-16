package com.minhvu.sso.service;

import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.dto.request.SignUpRequest;
import com.minhvu.sso.dto.response.UserProfileResponse;
import com.minhvu.sso.dto.response.page.PageData;
import com.minhvu.sso.dto.response.page.PageLink;
import com.minhvu.sso.model.enums.RoleType;

import java.util.UUID;

public interface UserService {

    AppUserDto save(AppUserDto userDto, AppUserDto currentUser);

    AppUserDto signUp(SignUpRequest signUpRequest);

    PageData<?> findUsers(
            PageLink pageLink,
            RoleType role,
            UUID contactId,
            AppUserDto currentUser,
            Long createdAtStartTs,
            Long createdAtEndTs,
            Boolean isEnabled,
            Boolean isSearchMatchCase
    );

    String getUserAvatarById(UUID id);

    UserProfileResponse getUserProfile(UUID id);

    String handleActiveUser(UUID id, Boolean isActive, AppUserDto currentUser);

    AppUserDto findByEmail(String email);

    String syncUsers();

}
