package com.infinity.fashionity.members.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class IdOrPasswordNotMatchedException extends CustomException {

    private static final ErrorCode ERROR_CODE = ErrorCode.CREDENTIAL_NOT_MATCHED;
    public IdOrPasswordNotMatchedException() {
        super(ERROR_CODE);
    }
}
