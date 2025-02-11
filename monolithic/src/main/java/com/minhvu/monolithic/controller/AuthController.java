package com.minhvu.monolithic.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.dto.request.ChangePasswordRequest;
import com.minhvu.monolithic.dto.request.RegisterRequest;
import com.minhvu.monolithic.dto.request.UpdateProfileRequest;
import com.minhvu.monolithic.dto.response.LoginResponse;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.exception.InvalidUsernameOrPassword;
import com.minhvu.monolithic.security.model.SecurityUser;
import com.minhvu.monolithic.security.model.token.JwtGenerator;
import com.minhvu.monolithic.security.service.RefreshTokenService;
import com.minhvu.monolithic.service.UserCredentialService;
import com.minhvu.monolithic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.Map;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("api/v1/auth")
public class AuthController extends BaseController {
    private final UserCredentialService userCredentialService;
    private final UserService userService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenService refreshTokenService;

    @Value(value = "${jwt.exp}")
    private Long jwtExp;

    @Value(value = "${jwt.refreshExp}")
    private Long jwtRefreshExp;

    @Autowired
    public AuthController(UserCredentialService userCredentialService, UserService userService, JwtGenerator jwtGenerator, RefreshTokenService refreshTokenService) {
        this.userCredentialService = userCredentialService;
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("signup")
    @Operation(summary = "Sign Up Customer (signUp)")
    public ResponseEntity<AppUserDto> signUp(
            @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(
                userService.signUp(registerRequest)
        );
    }

    @PostMapping("password/change")
    @Operation(summary = "Change password for current user (changePassword)")
    public ResponseEntity<LoginResponse> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        AppUser currentUser = getCurrentUser();
        userCredentialService.changePassword(currentUser, changePasswordRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        String token = jwtGenerator.generateToken(securityUser, jwtExp);
        String refreshToken = jwtGenerator.generateToken(securityUser, jwtRefreshExp);

        refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);

        return ResponseEntity.ok(
                new LoginResponse(token, refreshToken, jwtExp)
        );
    }

    @GetMapping("user")
    @Operation(summary = "Get current user (getCurrentUser)")
    public ResponseEntity<AppUserDto> getUserProfile() {
        return ResponseEntity.ok(
                userService.getUserProfile(getCurrentUser().getId(), getCurrentUser())
        );
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Update Account (saveUser)")
    public ResponseEntity<AppUserDto> saveUser(
            @Valid @RequestBody UpdateProfileRequest profile
    ) {
        AppUser currentUser = getCurrentUser();
        return ResponseEntity.ok(
                userService.save(profile, currentUser)
        );
    }
}
