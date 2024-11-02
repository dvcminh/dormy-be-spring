package com.minhvu.authservice.controller;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.minhvu.authservice.config.GoogleTokenVerifierService;
import com.minhvu.authservice.service.SecurityUserService;
import com.minhvu.authservice.dto.*;
import com.minhvu.authservice.dto.request.RefreshTokenRequest;
import com.minhvu.authservice.dto.response.LoginResponse;
import com.minhvu.authservice.dto.response.Response;
import com.minhvu.authservice.entity.AppUser;
import com.minhvu.authservice.entity.RefreshToken;
import com.minhvu.authservice.entity.SecurityUser;
import com.minhvu.authservice.exception.TokenRefreshException;
import com.minhvu.authservice.repository.UserCredentialsService;
import com.minhvu.authservice.service.AuthService;
import com.minhvu.authservice.service.RefreshTokenService;
import com.minhvu.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController extends BaseController{
    @Autowired
    private AuthService authService;
    @Autowired
    private SecurityUserService securityUserService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCredentialsService userCredentialsService;
    @Value(value = "${jwt.exp}")
    private Long jwtExp;

    @Value(value = "${jwt.refreshExp}")
    private Long jwtRefreshExp;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private GoogleTokenVerifierService googleTokenVerifierService;

    @PostMapping("/signup")
    @Operation(summary = "Sign Up Customer (signUp)")
    public ResponseEntity<AppUserDto> signUp(
            @RequestBody RegisterRequest registerRequest
    ) {
        AppUserDto appUserDto= authService.signUp(registerRequest);
        return ResponseEntity.ok(appUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> getToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            String token = authService.generateToken(securityUser, jwtExp);
            String refreshToken = authService.generateToken(securityUser, jwtRefreshExp);
            refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);
            return ResponseEntity.ok(new LoginResponse(token, refreshToken, jwtExp));
        } catch (Exception e) {
            throw new RuntimeException("invalid access");
        }
    }
    @PostMapping("token")
    @Operation(summary = "Refresh Token (refreshToken)")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    SecurityUser securityUser = securityUserService.loadUserByUsername(user.getEmail());
                    String token = authService.generateToken(securityUser, jwtExp);
                    String refreshToken = authService.generateToken(securityUser, jwtRefreshExp);
                    refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);

                    return ResponseEntity.ok(new LoginResponse(token, refreshToken, jwtExp));
                })
                .orElseThrow(() -> {
                    return new TokenRefreshException(
                            request.getRefreshToken(),
                            "Refresh token is not found!"
                    );
                });
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<Page<AppUser>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        Page<AppUser> userPage = userService.getAllUsers(PageRequest.of(page, size, Sort.by(direction, sortBy)));
        return ResponseEntity.ok(userPage);
    }
    @PostMapping("password/change")
    @Operation(summary = "Change password for current user (changePassword)")
    public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        AppUserDto currentUser = getCurrentUser();
        userCredentialsService.changePassword(currentUser, changePasswordRequest);
        return ResponseEntity.ok(new Response("Password updated successfully"));
    }
    @PostMapping("/updateUser")
    public String updateUser(@RequestBody UpdateUserRequest userDto) {
        return userService.updateUser(userDto);
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Delete User successfully");
    }
    @GetMapping("/user")
    public OAuth2User getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return oAuth2User;
    }
    @PostMapping("/google-login")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody String idTokenString) {
        GoogleIdToken idToken = googleTokenVerifierService.verifyToken(idTokenString);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            AppUser user = authService.processGoogleUser(payload);

            String accessToken = authService.generateTokenForOauth2(user, jwtExp);
            String refreshToken = authService.generateTokenForOauth2(user, jwtRefreshExp);

            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, jwtExp));
        } else {
            throw new RuntimeException("Invalid access");
        }
    }
}
