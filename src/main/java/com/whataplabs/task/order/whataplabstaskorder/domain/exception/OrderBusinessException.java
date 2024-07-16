package com.whataplabs.task.order.whataplabstaskorder.domain.exception;

public abstract class OrderBusinessException extends RuntimeException {
    public OrderBusinessException() {
    }

    public OrderBusinessException(String message) {
        super(message);
    }

    public abstract ErrorType getErrorType();
    public String getErrorCode() {
        return getErrorType().getCode();
    }

    public String getErrorMessage(){
        return getMessage() == null || getMessage().isBlank() ? getErrorType().getDefaultMessage() : getMessage();
    }
}
