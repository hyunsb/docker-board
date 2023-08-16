package com.hyunsb.wanted._core.error.exception;

public class BoardDeleteFailureException extends InternalServerErrorException {

    public BoardDeleteFailureException() {
    }

    public BoardDeleteFailureException(String message) {
        super(message);
    }
}
