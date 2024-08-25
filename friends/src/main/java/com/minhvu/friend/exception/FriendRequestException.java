package com.minhvu.friend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FriendRequestException  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FriendRequestException(String message) {
        super(message);
    }
}