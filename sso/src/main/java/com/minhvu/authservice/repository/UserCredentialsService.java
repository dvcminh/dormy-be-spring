package com.minhvu.authservice.repository;



import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.ChangePasswordRequest;
import com.minhvu.authservice.entity.AppUser;

import java.util.UUID;


public interface UserCredentialsService {

    boolean isEnabled(UUID userId);

    void changePassword(AppUserDto currentUser, ChangePasswordRequest passwordRequest);

    void setPassword(UUID uuid, String password);

}
