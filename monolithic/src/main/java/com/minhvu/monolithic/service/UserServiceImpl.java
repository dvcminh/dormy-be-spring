package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.mapper.AppUserMapper;
import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.dto.request.RegisterRequest;
import com.minhvu.monolithic.dto.request.UpdateProfileRequest;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.exception.BadRequestException;
import com.minhvu.monolithic.exception.UnAuthorizedException;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.enums.RoleType;
import com.minhvu.monolithic.repository.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final AppUserRepository userRepository;
    private final UserCredentialService userCredentialService;
    private final AppUserMapper mapper;

    @Autowired
    public UserServiceImpl(AppUserRepository userRepository, UserCredentialService userCredentialService, AppUserMapper mapper) {
        this.userRepository = userRepository;
        this.userCredentialService = userCredentialService;
        this.mapper = mapper;
    }

    @Override
    public PageData<AppUserDto> findUsers(int page, int pageSize, AppUser currentUser) {
        if (!checkIfAdmin(currentUser)) {
            throw new UnAuthorizedException("You do not have permission to do this action");
        }
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<AppUserDto> userDtoPage = userRepository.findAllByRole(RoleType.USER, pageable)
                .map(mapper::toDto);
        return new PageData<>(userDtoPage);
    }

    @Override
    public AppUserDto getUserProfile(UUID id, AppUser currentUser) {
        if (id == currentUser.getId() || currentUser.getRole() == RoleType.ADMIN) {
            return mapper.toDto(currentUser);
        } else {
            throw new UnAuthorizedException("You do not have permission to do this action");
        }
    }

    @Override
    public AppUserDto save(UpdateProfileRequest profile, AppUser currentUser) {
        currentUser.setDisplayName(profile.getDisplayName());
        AppUser savedUser = userRepository.save(currentUser);
        return mapper.toDto(savedUser);
    }

    @Override
    public AppUserDto signUp(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        AppUser newUser = userRepository.save(
                AppUser.builder()
                        .username(registerRequest.getUsername())
                        .role(RoleType.USER)
                        .build()
        );

        userCredentialService.setPassword(newUser.getId(), registerRequest.getPassword());
        return mapper.toDto(newUser);
    }

    @Override
    public ResponseEntity<AppUserDto> getProfile(UUID id) {
        AppUser appUser = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User not found"));
        return ResponseEntity.ok(mapper.toDto(appUser));
    }

    private boolean checkIfAdmin(AppUser currentUser) {
        return currentUser.getRole() == RoleType.ADMIN;
    }

    @Override
    public ResponseEntity<String> changeAccountType(UUID id, AccountType type, AppUser userPrinciple) {
        Optional<AppUser> user = userRepository.findById(id);

        //extracting email from userPrinciple //since we use authentication using email so username return email
        //for more clarity check userPrinciple class
        String email = userPrinciple.getUsername();


        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        AppUser retrivedUser = user.get();

        //checking only login user can change their account type
        if (!email.equals(retrivedUser.getUsername())) {
            return ResponseEntity.status((HttpStatus.UNAUTHORIZED)).body("You are not authorised change account type");
        }
        retrivedUser.setAccountType(type);

        try {
            userRepository.save(retrivedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong, please try again letter");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Account type changed successfully!");
    }

}


