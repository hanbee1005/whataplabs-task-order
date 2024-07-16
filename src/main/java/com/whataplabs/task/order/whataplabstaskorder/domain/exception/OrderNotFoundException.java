package com.whataplabs.task.order.whataplabstaskorder.domain.exception;

public class OrderNotFoundException extends OrderBusinessException {
    private final Long orderId;

    public OrderNotFoundException(Long orderId) {
        super("존재하지 않는 주문입니다. id=" + orderId);
        this.orderId = orderId;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.NOT_FOUND_ORDER;
    }
}
