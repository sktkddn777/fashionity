package com.infinity.fashionity.members.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class MemberNotFoundException extends CustomException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_NOT_FOUND;

    public MemberNotFoundException() {
        super(ERROR_CODE);
    }
}