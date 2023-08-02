package com.infinity.fashionity.global.exception;

/**
 * 입력값이 잘못되었을 때 발생하는 EXCEPTION
 * */
public class ValidationException extends CustomException{
    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
