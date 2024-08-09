package com.minhvu.review.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MediaException extends RuntimeException {

    public MediaException(String message) {
        super(message);
    }

    public MediaException(String message, Throwable cause) {
        super(message, cause);
    }
}