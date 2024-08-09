package com.minhvu.friend.openfeign;

import com.minhvu.friend.dto.UserDTO;
import com.minhvu.friend.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient{
    @Override
    public boolean userExists(Long userId) {
        return false;
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        throw new UserNotFoundException("Error lors de recuperation de user.");
    }
}
