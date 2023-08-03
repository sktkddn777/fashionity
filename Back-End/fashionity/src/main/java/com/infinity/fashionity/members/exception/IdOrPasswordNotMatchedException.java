package com.infinity.fashionity.members.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class IdOrPasswordNotMatchedException extends CustomException {

    public IdOrPasswordNotMatchedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
