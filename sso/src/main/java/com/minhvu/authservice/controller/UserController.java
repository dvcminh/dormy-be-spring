package com.minhvu.authservice.controller;

import com.minhvu.authservice.dto.AppUserDto;
import com.minhvu.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @GetMapping("/exist/{userId}")
    public boolean userExists(@PathVariable UUID userId) {
        return userService.checkIfUserExist(userId);
    }
    @GetMapping("/{userId}")
    ResponseEntity<AppUserDto> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
