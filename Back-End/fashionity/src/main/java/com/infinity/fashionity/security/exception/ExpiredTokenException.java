package com.infinity.fashionity.security.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class ExpiredTokenException extends CustomException {

    public ExpiredTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}

