package com.infinity.fashionity.auth.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class MailSendException extends CustomException {

    public MailSendException(ErrorCode errorCode) {
        super(errorCode);
    }
}
