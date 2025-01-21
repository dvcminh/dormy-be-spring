package com.minhvu.interaction.exception;

import jakarta.naming.AuthenticationException;

public class JwtTokenMalformedException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public JwtTokenMalformedException(String msg) {
        super(msg);
    }
}
