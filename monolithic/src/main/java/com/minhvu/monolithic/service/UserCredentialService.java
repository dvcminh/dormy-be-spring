package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.request.ChangePasswordRequest;
import com.minhvu.monolithic.entity.AppUser;

import java.util.UUID;

public interface UserCredentialService {
    void changePassword(AppUser currentUser, ChangePasswordRequest passwordRequest);

    void setPassword(UUID userId, String password);

    void setPassword(UUID userId);
}
