package com.infinity.fashionity.comments.exception;

import com.infinity.fashionity.global.exception.ErrorCode;

public class NotFoundException extends RuntimeException{
    public NotFoundException(ErrorCode errorCode){

    }
}
