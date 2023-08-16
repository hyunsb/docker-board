package com.hyunsb.wanted._core.error.exception;

import com.hyunsb.wanted._core.error.ErrorMessage;

public class ExceededMaximumPageSizeException extends InternalServerErrorException {

    public ExceededMaximumPageSizeException() {
        super(ErrorMessage.EXCEEDED_SIZE);
    }

    public ExceededMaximumPageSizeException(String message) {
        super(message);
    }
}
