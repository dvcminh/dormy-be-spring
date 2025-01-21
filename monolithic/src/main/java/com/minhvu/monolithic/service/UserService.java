package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.dto.request.RegisterRequest;
import com.minhvu.monolithic.dto.request.UpdateProfileRequest;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.enums.AccountType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserService {
    PageData<AppUserDto> findUsers(
            int page,
            int pageSize,
            AppUser currentUser
    );

    AppUserDto getUserProfile(UUID id, AppUser currentUser);

    AppUserDto save(UpdateProfileRequest profile, AppUser currentUser);

    AppUserDto signUp(RegisterRequest registerRequest);

    ResponseEntity<AppUserDto> getProfile(UUID id);

    ResponseEntity<String> changeAccountType(UUID id, AccountType type, AppUser userPrinciple);
}

