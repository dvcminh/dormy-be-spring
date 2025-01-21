package com.minhvu.monolithic.controller;

import com.minhvu.monolithic.dto.request.LoginRequest;
import com.minhvu.monolithic.dto.request.RefreshTokenRequest;
import com.minhvu.monolithic.dto.response.LoginResponse;
import com.minhvu.monolithic.exception.InvalidUsernameOrPassword;
import com.minhvu.monolithic.repository.AppUserRepository;
import com.minhvu.monolithic.security.exception.TokenRefreshException;
import com.minhvu.monolithic.security.model.RefreshToken;
import com.minhvu.monolithic.security.model.SecurityUser;
import com.minhvu.monolithic.security.model.token.JwtGenerator;
import com.minhvu.monolithic.security.service.CustomUserDetailsService;
import com.minhvu.monolithic.security.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@Slf4j
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class LoginController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final AppUserRepository appUserRepository;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService securityUserService;
    @Value(value = "${jwt.exp}")
    private Long jwtExp;
    @Value(value = "${jwt.refreshExp}")
    private Long jwtRefreshExp;

    @PostMapping("login")
    @Operation(summary = "Login method to get user JWT token data (loginEndpoint)")
    public ResponseEntity<LoginResponse> loginEndpoint(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            String token = jwtGenerator.generateToken(securityUser, jwtExp);
            String refreshToken = jwtGenerator.generateToken(securityUser, jwtRefreshExp);

//            refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);
            return ResponseEntity.ok(
                    new LoginResponse(token, refreshToken, jwtExp)
            );
        } catch (AuthenticationException e) {
            throw new InvalidUsernameOrPassword("Incorrect email or password");
        }
    }

    @PostMapping("token")
    @Operation(summary = "Refresh Token (refreshToken)")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    SecurityUser securityUser = securityUserService.loadUserByUsername(user.getUsername());
                    String token = jwtGenerator.generateToken(securityUser, jwtExp);
                    String refreshToken = jwtGenerator.generateToken(securityUser, jwtRefreshExp);
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
}
