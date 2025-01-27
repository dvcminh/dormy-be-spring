package com.minhvu.monolithic.controller;


import com.minhvu.monolithic.dto.mapper.AppUserMapper;
import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping("/api/v1/user")
@RestController
public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private AppUserMapper appUserMapper;

    //Api to View Profile
    @GetMapping("/{id}")
    private ResponseEntity<AppUserDto> getProfile(@PathVariable UUID id){
        return userService.getProfile(id);
    }

    //Api to convert account type
    @PutMapping("/{id}/{type}")
    private ResponseEntity<String> changeAccountType(@PathVariable UUID id, @PathVariable AccountType type){
        AppUser userPrinciple = getCurrentUser();
        return userService.changeAccountType(id,type,userPrinciple);
    }
}
