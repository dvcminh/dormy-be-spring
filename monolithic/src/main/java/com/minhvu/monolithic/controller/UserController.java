package com.minhvu.monolithic.controller;


import com.minhvu.monolithic.dto.mapper.AppUserMapper;
import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.enums.RoleType;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/api/v1/user")
@RestController
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppUserMapper appUserMapper;

    //Api to get all users
    @GetMapping
    public ResponseEntity<PageData<AppUserDto>> getUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int pageSize) {
        AppUser userPrinciple = getCurrentUser();
        return ResponseEntity.ok(userService.findUsers(page, pageSize, userPrinciple));
    }

    @GetMapping("/active")
    public ResponseEntity<List<AppUserDto>> getActiveUsers() {
        AppUser userPrinciple = getCurrentUser();
        if (!checkIfAdmin(userPrinciple)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @GetMapping("/banned")
    public ResponseEntity<List<AppUserDto>> getBannedUsers() {
        AppUser userPrinciple = getCurrentUser();
        if (!checkIfAdmin(userPrinciple)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.getBannedUsers());
    }

    private boolean checkIfAdmin(AppUser user) {
        return user.getRole() == RoleType.ADMIN;
    }

    //Api to View Profile
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getProfile(@PathVariable UUID id) {
        return userService.getProfile(id);
    }

    //Api to convert account type
    @PutMapping("/{id}/{type}")
    public ResponseEntity<String> changeAccountType(@PathVariable UUID id, @PathVariable AccountType type) {
        AppUser userPrinciple = getCurrentUser();
        return userService.changeAccountType(id, type, userPrinciple);
    }

    //Api to search user
    @GetMapping("/search")
    public ResponseEntity<List<AppUserDto>> searchUser(@RequestParam String displayName) {
        return userService.searchUser(displayName);
    }

    //Api for unban and ban user
    @PutMapping("/ban/{id}")
    public ResponseEntity<String> banUser(@PathVariable UUID id, @RequestParam Boolean ban) {
        AppUser userPrinciple = getCurrentUser();
        return userService.banUser(id, userPrinciple, ban);
    }

    @PutMapping("/ban/batch")
    public ResponseEntity<String> batchBanUsers(
        @RequestParam Boolean ban,
        @RequestBody List<UUID> userIds
    ) {
        AppUser userPrinciple = getCurrentUser();
        return userService.batchBanUsers(userIds, userPrinciple, ban);
    }

    @GetMapping("/email/{userEmail}")
    public ResponseEntity<AppUserDto> getUser(@PathVariable String userEmail) {
        return ResponseEntity.ok().body(appUserMapper.toDto(userService.getUser(userEmail)));
    }
}
