package com.minhvu.friend.openfeign;

import com.minhvu.friend.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SSO-SERVICE/api/v1/users")
public interface UserClient {

    @GetMapping("/exist/{userId}")
     boolean userExists(@PathVariable Long userId);

    @GetMapping("/{userId}")
    ResponseEntity<UserDTO> getUserById(@PathVariable Long userId);
}
