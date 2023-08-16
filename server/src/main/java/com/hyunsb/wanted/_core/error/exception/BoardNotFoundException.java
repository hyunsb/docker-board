package com.hyunsb.wanted._core.error.exception;

import com.hyunsb.wanted._core.error.ErrorMessage;

public class BoardNotFoundException extends InternalServerErrorException {

    public BoardNotFoundException() {
        super(ErrorMessage.INVALID_BOARD);
    }

    public BoardNotFoundException(String message) {
        super(message);
    }
}
