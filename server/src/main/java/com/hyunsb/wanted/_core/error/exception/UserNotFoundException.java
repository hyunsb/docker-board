package com.hyunsb.wanted._core.error.exception;

public class UserNotFoundException extends UnauthorizedException {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
