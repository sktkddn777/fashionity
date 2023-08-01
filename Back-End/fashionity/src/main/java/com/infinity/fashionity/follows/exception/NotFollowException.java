package com.infinity.fashionity.follows.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class NotFollowException extends CustomException {

    public NotFollowException(ErrorCode errorCode) {
        super(errorCode);
    }
}
