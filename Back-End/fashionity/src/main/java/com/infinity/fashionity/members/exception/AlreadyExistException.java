package com.infinity.fashionity.members.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class AlreadyExistException extends CustomException {

    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
