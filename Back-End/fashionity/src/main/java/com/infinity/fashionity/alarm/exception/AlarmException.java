package com.infinity.fashionity.alarm.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class AlarmException extends CustomException {
    public AlarmException(ErrorCode errorCode) {
        super(errorCode);
    }
}
