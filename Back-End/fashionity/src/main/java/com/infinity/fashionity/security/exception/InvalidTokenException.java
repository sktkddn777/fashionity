package com.infinity.fashionity.security.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException() {
        super("토큰 정보가 잘못되었습니다.");
    }
}
