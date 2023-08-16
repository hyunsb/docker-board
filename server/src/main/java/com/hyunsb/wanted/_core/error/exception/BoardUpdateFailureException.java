package com.hyunsb.wanted._core.error.exception;

public class BoardUpdateFailureException extends InternalServerErrorException {

    public BoardUpdateFailureException() {
    }

    public BoardUpdateFailureException(String message) {
        super(message);
    }
}
