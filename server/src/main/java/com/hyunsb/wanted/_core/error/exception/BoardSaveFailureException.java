package com.hyunsb.wanted._core.error.exception;

public class BoardSaveFailureException extends InternalServerErrorException {

    public BoardSaveFailureException() {
    }

    public BoardSaveFailureException(String message) {
        super(message);
    }
}
