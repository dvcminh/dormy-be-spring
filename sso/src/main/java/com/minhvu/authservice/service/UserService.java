package com.minhvu.authservice.service;

import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.UpdateUserRequest;
import com.minhvu.authservice.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    AppUserDto findByEmail(String email);
    Page<AppUser> getAllUsers(Pageable pageable);
    String updateUser(UpdateUserRequest userDto);
    void deleteUser(UUID id);
    boolean checkIfUserExist(UUID id);
    public Optional<AppUser> getAuthenticatedUser();
    String syncUsers();
    AppUserDto getUserById(UUID userId);
}
