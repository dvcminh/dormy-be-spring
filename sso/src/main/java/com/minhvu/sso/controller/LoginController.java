package com.minhvu.sso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhvu.sso.dto.model.AppUserDto;
import com.minhvu.sso.dto.model.LogDto;
import com.minhvu.sso.dto.request.LoginRequest;
import com.minhvu.sso.dto.request.RefreshTokenRequest;
import com.minhvu.sso.dto.response.LoginResponse;
import com.minhvu.sso.dto.response.Response;
import com.minhvu.sso.exception.InvalidUsernameOrPassword;
import com.minhvu.sso.model.AppUser;
import com.minhvu.sso.model.enums.ActionStatus;
import com.minhvu.sso.model.enums.ActionType;
import com.minhvu.sso.security.exception.TokenRefreshException;
import com.minhvu.sso.security.model.RefreshToken;
import com.minhvu.sso.security.model.SecurityUser;
import com.minhvu.sso.security.model.token.JwtTokenFactory;
import com.minhvu.sso.security.service.RefreshTokenService;
import com.minhvu.sso.security.service.SecurityUserService;
import com.minhvu.sso.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/auth")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityUserService securityUserService;

    @Autowired
    JwtTokenFactory jwtTokenFactory;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    LogService logService;

    @Value(value = "${jwt.exp}")
    private Long jwtExp;

    @Value(value = "${jwt.refreshExp}")
    private Long jwtRefreshExp;

    @PostMapping("login")
    @Operation(summary = "Login method to get user JWT token data (loginEndpoint)")
    public ResponseEntity<LoginResponse> loginEndpoint(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            String token = jwtTokenFactory.generateToken(securityUser, jwtExp);
            String refreshToken = jwtTokenFactory.generateToken(securityUser, jwtRefreshExp);

            refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);
            ObjectMapper objectMapper = new ObjectMapper();
            logService.save(LogDto.builder()
                    .entityId(securityUser.getUser().getId())
                    .actionData(objectMapper.valueToTree(loginRequest))
                    .actionStatus(ActionStatus.SUCCESS)
                    .actionType(ActionType.LOGIN)
                    .build(), securityUser.getUser().getId());
            return ResponseEntity.ok(
                    new LoginResponse(token, refreshToken, jwtExp)
            );
        } catch (AuthenticationException e) {
            ObjectMapper objectMapper = new ObjectMapper();
            logService.save(LogDto.builder()
                    .actionData(objectMapper.valueToTree(loginRequest))
                    .actionStatus(ActionStatus.FAILURE)
                    .actionType(ActionType.LOGIN)
                    .actionFailureDetails("Incorrect email or password").build(), new AppUserDto());
            throw new InvalidUsernameOrPassword("Incorrect email or password");
        }
    }

    @PostMapping("logout")
    @Operation(summary = "Logout (logout)")
    public ResponseEntity<Response> logout(HttpServletRequest request) {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principle.toString().equals("anonymousUser")) {
            AppUser user = ((SecurityUser) principle).getUser();
            UUID userId = user.getId();
            refreshTokenService.deleteByUserId(userId);
            ObjectMapper objectMapper = new ObjectMapper();
            logService.save(LogDto.builder()
                    .entityId(user.getId())
                    .actionData(objectMapper.valueToTree(null))
                    .actionType(ActionType.LOGOUT)
                    .actionStatus(ActionStatus.SUCCESS)
                    .build(), user.getId());
        }
        return ResponseEntity.ok(new Response("You've been signed out!"));
    }

    @PostMapping("token")
    @Operation(summary = "Refresh Token (refreshToken)")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    SecurityUser securityUser = securityUserService.loadUserByUsername(user.getEmail());
                    String token = jwtTokenFactory.generateToken(securityUser, jwtExp);
                    String refreshToken = jwtTokenFactory.generateToken(securityUser, jwtRefreshExp);
                    refreshTokenService.createRefreshToken(securityUser.getUser(), refreshToken);
                    ObjectMapper objectMapper = new ObjectMapper();
                    logService.save(LogDto.builder()
                                    .entityId(securityUser.getUser().getId())
                                    .actionStatus(ActionStatus.SUCCESS)
                                    .actionData(objectMapper.valueToTree(request))
                                    .actionType(ActionType.LOGIN).build(),
                            securityUser.getUser().getId());
                    return ResponseEntity.ok(new LoginResponse(token, refreshToken, jwtExp));
                })
                .orElseThrow(() -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    logService.save(LogDto.builder()
                            .actionData(objectMapper.valueToTree(request))
                            .actionStatus(ActionStatus.FAILURE)
                            .actionType(ActionType.LOGIN)
                            .actionFailureDetails("Refresh token is not found!").build(), new AppUserDto());
                    return new TokenRefreshException(
                            request.getRefreshToken(),
                            "Refresh token is not found!"
                    );
                });
    }
}
