package com.minhvu.authservice.exception;

import com.minhvu.authservice.exception.response.UserErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class UserErrorResponseHandler {
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundException exception) {
        UserErrorResponse userErrorResponse = new UserErrorResponse();
        userErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        userErrorResponse.setMessage(exception.getMessage());
        userErrorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);
    }
}
