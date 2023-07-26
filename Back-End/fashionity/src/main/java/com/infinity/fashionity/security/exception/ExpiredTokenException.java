package com.infinity.fashionity.security.exception;

public class ExpiredTokenException extends RuntimeException{

    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException() {
        super("토큰 기간이 만료되었습니다.");
    }
}

