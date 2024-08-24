package com.minhvu.authservice.service;

import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.dto.UpdateUserRequest;
import com.minhvu.authservice.entity.AppUser;
import com.minhvu.authservice.exception.UserNotFoundException;
import com.minhvu.authservice.kafka.UserProducer;
import com.minhvu.authservice.mapper.UserMapper;
import com.minhvu.authservice.repository.AppUserRepository;
import com.minhvu.authservice.repository.UserCredentialsRepository;
import com.minhvu.authservice.repository.UserCredentialsService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserCredentialsService userCredentialService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserCredentialsRepository repository;
    @Autowired
    private UserProducer userProducer;
    public AppUser getUserByUserName(String name) {
        return appUserRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email %s not found", name)));
    }
    @Override
    public AppUserDto findByEmail(String email) {
        AppUser user = appUserRepository.findByEmail(email);
        return userMapper.toDto(user);
    }
    @Override
    public boolean checkIfUserExist(UUID id) {
        return appUserRepository.existsById(id);
    }
    @Override
    public Optional<AppUser> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return appUserRepository.findByName(username);
    }

    @Override
    public String syncUsers() {
        appUserRepository.findAll().forEach(appUser -> {
            userProducer.sendMessage(userMapper.toDto(appUser));
        });
        return "Sync users successfully";
    }

    @Override
    public AppUserDto getUserById(UUID userId) {
        return userMapper.toDto(appUserRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found")));
    }

    @Override
    public Page<AppUser> getAllUsers(Pageable pageable) {
        return appUserRepository.findAll(pageable);
    }
    @Override
    public String updateUser(UpdateUserRequest userDto) {
        Optional<AppUser> user = appUserRepository.findById(userDto.getId());
        if (user.isPresent()) {
            user.get().setName(userDto.getName());
            user.get().setEmail(userDto.getEmail());
            user.get().setAddress(userDto.getAddress());
            user.get().setPhone(userDto.getPhone());
            user.get().setAvatar(userDto.getAvatar());
            appUserRepository.save(user.get());
            return "User updated successfully";
        } else {
            throw new UserNotFoundException("User not found");
        }
    }
    @Override
    public void deleteUser(UUID id) {
        appUserRepository.deleteById(id);
    }
}
