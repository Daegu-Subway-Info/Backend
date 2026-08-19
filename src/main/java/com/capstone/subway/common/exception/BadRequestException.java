package com.capstone.subway.common.exception;

import com.capstone.subway.common.ErrorCode;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
