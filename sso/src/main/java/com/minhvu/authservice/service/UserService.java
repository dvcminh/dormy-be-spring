package com.minhvu.authservice.service;

import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.UpdateUserInformationRequest;
import com.minhvu.authservice.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    AppUserDto findByEmail(String email);
    Page<AppUser> getAllUsers(Pageable pageable);
    String updateUser(UpdateUserInformationRequest userDto);
    void deleteUser(Long id);
    boolean checkIfUserExist(Long id);
    public Optional<AppUser> getAuthenticatedUser();

    AppUserDto getUserById(Long userId);
}
