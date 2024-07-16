package com.minhvu.sso.service;


import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.dto.request.ChangePasswordRequest;

import java.util.UUID;

public interface UserCredentialsService {

    boolean isEnabled(UUID userId);

    void changePassword(AppUserDto currentUser, ChangePasswordRequest passwordRequest);

    void setPassword(UUID userId, String password);

    void setPassword(UUID userId);
}
