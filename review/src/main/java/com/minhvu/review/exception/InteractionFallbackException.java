package com.minhvu.review.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InteractionFallbackException extends RuntimeException {

    public InteractionFallbackException(String message) {
        super(message);
    }

    public InteractionFallbackException(String message, Throwable cause) {
        super(message, cause);
    }
}