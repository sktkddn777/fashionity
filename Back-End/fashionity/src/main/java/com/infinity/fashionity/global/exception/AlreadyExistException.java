package com.infinity.fashionity.global.exception;


public class AlreadyExistException extends CustomException {
    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
