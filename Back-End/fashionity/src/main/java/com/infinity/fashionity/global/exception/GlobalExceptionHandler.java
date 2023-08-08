package com.infinity.fashionity.global.exception;

import com.infinity.fashionity.global.dto.ErrorResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static com.infinity.fashionity.global.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
@Getter
public class GlobalExceptionHandler {

    // CustomException을 상속받은 모든 에러를 처리하는 Handler
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }

    // CustomException을 상속받지 않은 에러를 처리하는 Handler들 작성
    // @Valid 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(BindException e) {
        log.error("[EXCEPTION] {}", e.getClass().getSimpleName());
        return ResponseEntity.status(BAD_REQUEST)
                .body(new ErrorResponse(INVALID_INPUT_VALUE.getCode(), e.getFieldError().getDefaultMessage()));
    }

    // 요청 데이터 인자 부족
    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestValueException(MissingRequestValueException e) {
        log.error("[EXCEPTION] {}", e.getClass().getSimpleName());
        e.printStackTrace();
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(MISSING_INPUT_VALUE));
    }

    // PathVariable 타입이 MissMatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        log.error("[EXCEPTION] {}", e.getClass().getSimpleName());
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(INVALID_PATH_VALUE));
    }

    // 잘못된 HttpMethod로 요청
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[EXCEPTION] {}", e.getClass().getSimpleName());
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(METHOD_NOT_ALLOWED));
    }

    // 없는 api로 요청
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("[EXCEPTION] {}", e.getClass().getSimpleName());
        return ResponseEntity.status(BAD_REQUEST)
                .body(ErrorResponse.of(NOT_EXIST_API));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("[EXCEPTION] {}", e.getClass().getSimpleName());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
