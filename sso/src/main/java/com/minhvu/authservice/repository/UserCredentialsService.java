package com.minhvu.authservice.repository;



import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.ChangePasswordRequest;


public interface UserCredentialsService {

    boolean isEnabled(Long userId);

    void changePassword(AppUserDto currentUser, ChangePasswordRequest passwordRequest);

    void setPassword(Long userId, String password);

    void setPassword(Long userId);
}
