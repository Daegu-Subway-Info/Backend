package com.capstone.subway.common.exception;

import com.capstone.subway.common.ErrorCode;

public class NotFoundException extends ApiException {

    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
