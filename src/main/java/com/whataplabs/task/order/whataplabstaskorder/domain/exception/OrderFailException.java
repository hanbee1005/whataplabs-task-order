package com.whataplabs.task.order.whataplabstaskorder.domain.exception;

import lombok.Getter;

@Getter
public class OrderFailException extends OrderBusinessException {
    private final Long orderId;
    private final String externalErrorMessage;

    public OrderFailException(Long orderId, String errorMessage) {
        super("주문에 실패하였습니다. id=" + orderId);
        this.orderId = orderId;
        this.externalErrorMessage = errorMessage;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ORDER_FAIL;
    }
}
