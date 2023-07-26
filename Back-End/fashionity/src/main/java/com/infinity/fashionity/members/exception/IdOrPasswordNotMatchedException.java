package com.infinity.fashionity.members.exception;

public class IdOrPasswordNotMatchedException extends RuntimeException{

    public IdOrPasswordNotMatchedException(String message) {
        super(message);
    }

    public IdOrPasswordNotMatchedException() {
        super("아이디 혹은 비밀번호가 잘못됐습니다.");
    }
}
