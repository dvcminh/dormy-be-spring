package com.minhvu.monolithic.controller;


import com.minhvu.monolithic.dto.LoginDto;
import com.minhvu.monolithic.dto.ResetPasswordDto;
import com.minhvu.monolithic.dto.UserDto;
import com.minhvu.monolithic.entity.User;
import com.minhvu.monolithic.entity.UserPrinciple;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //Api to register user
    @PostMapping("/register")
    private ResponseEntity<String> registerUser(@RequestBody User user) {
        return userService.register(user);
    }


    //Api to check username available or not
    @GetMapping("/check-username/{username}")
    private ResponseEntity <String> checkUsername(@PathVariable String username ){
        return userService.checkUsername(username);
    }


    //Api to View Profile
    @GetMapping("/{id}")
    private ResponseEntity<?> getProfile(@PathVariable Long id){
        return userService.getProfile(id);
    }


   //Api to update user details
    @PutMapping("/{id}")
    private ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDto userDetails,
                                              @AuthenticationPrincipal UserPrinciple userPrinciple){
        return userService.updateUser(id,userDetails,userPrinciple);
    }

    //Api to convert account type
    @PutMapping("/{id}/{type}")
    private ResponseEntity<String> changeAccountType(@PathVariable Long id, @PathVariable AccountType type,
                                                     @AuthenticationPrincipal UserPrinciple userPrinciple){
        return userService.changeAccountType(id,type,userPrinciple);
    }

    //Api to login
    @PostMapping("/login")
    private ResponseEntity<String> userLogin(@RequestBody LoginDto loginDetails){
        return userService.verify(loginDetails);
    }

    //api for making request to forgot password
    @PostMapping("/forgotPassword/{email}")
    private ResponseEntity<String> forgotPassword(@PathVariable String email){
        return userService.forgotPassword(email);
    }

    //api for rest password
    @PutMapping("/restPassword/{userId}/{token}")
    private ResponseEntity<String> resetPassword(@PathVariable String token,
                                                 @RequestBody  ResetPasswordDto password,
                                                 @PathVariable Long userId){

        return userService.resetPassword(token,password,userId);
    }


    //api to fetch user by userName
    @GetMapping("/user/{userName}")
    private ResponseEntity<?> fetchUserByUserName(@PathVariable String userName){
        return userService.fetchUserByUserName(userName);
    }


}
