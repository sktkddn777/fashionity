package com.infinity.fashionity.security.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class InvalidTokenException extends CustomException {

    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
