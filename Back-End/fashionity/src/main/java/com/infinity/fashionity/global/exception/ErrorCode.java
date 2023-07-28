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
    INVALID_PATH_VALUE(HttpStatus.BAD_REQUEST,"C006","요청이 잘못됐습니다."),

    // Auth
    CREDENTIAL_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 일치하지 않습니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 사용자입니다."),

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"P001","존재하지 않는 포스트입니다."),
    POST_REPORT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "P002", "이미 신고한 게시글입니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"CM001","존재하지 않는 댓글입니다."),
    COMMENT_REPORT_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"CM002","이미 신고한 댓글입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
