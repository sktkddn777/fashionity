package com.infinity.fashionity.follows.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class AlreadyFollowException extends CustomException {

    public AlreadyFollowException(ErrorCode errorCode) {
        super(errorCode);
    }
}
