package com.infinity.fashionity.members.exception;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException() {
        super("해당 멤버를 찾을 수 없습니다.");
    }
}