package com.infinity.fashionity.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력 값입니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "인자가 부족합니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C003", "접근권한이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C004", "사용할 수 없는 메서드입니다."),
    NOT_EXIST_API(HttpStatus.BAD_REQUEST, "C005", "요청 주소가 올바르지 않습니다."),

    // Auth
    CREDENTIAL_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 일치하지 않습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 사용자입니다."),
    EXIST_MEMBER_ID(HttpStatus.BAD_REQUEST, "M002", "이미 존재하는 아이디입니다."),
    EXIST_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "M003", "이미 존재하는 이메일입니다."),
    EXIST_MEMBER_NICKNAME(HttpStatus.BAD_REQUEST, "M004", "이미 존재하는 닉네임입니다."),
    INVALID_MEMBER_ID(HttpStatus.BAD_REQUEST, "M005", "아이디 제약조건에 맞지 않습니다."),
    INVALID_MEMBER_NICKNAME(HttpStatus.BAD_REQUEST, "M006", "닉네임 제약조건에 맞지 않습니다."),
    INVALID_MEMBER_PASSWORD(HttpStatus.BAD_REQUEST, "M007", "비밀번호 제약조건에 맞지 않습니다."),
    INVALID_MEMBER_EMAIL(HttpStatus.BAD_REQUEST, "M008", "이메일 제약조건에 맞지 않습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
