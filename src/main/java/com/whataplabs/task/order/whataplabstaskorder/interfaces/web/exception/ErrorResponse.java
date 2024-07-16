package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.exception;

import com.whataplabs.task.order.whataplabstaskorder.domain.exception.ErrorType;

public record ErrorResponse(
        String code,
        String message
) {
    public static ErrorResponse createInternalServerError() {
        return new ErrorResponse(ErrorType.INTERNAL_SERVER_ERROR.getCode(), ErrorType.INTERNAL_SERVER_ERROR.getDefaultMessage());
    }
}
