package com.infinity.fashionity.global.exception;

public class AccessDeniedException extends CustomException{
    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
