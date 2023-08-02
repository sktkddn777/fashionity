package com.infinity.fashionity.image.exception;

import com.infinity.fashionity.global.exception.CustomException;
import com.infinity.fashionity.global.exception.ErrorCode;

public class ImageException extends CustomException {
    public ImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
