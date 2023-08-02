package com.infinity.fashionity.members.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class CustomValidationException extends CustomException {
    public CustomValidationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
