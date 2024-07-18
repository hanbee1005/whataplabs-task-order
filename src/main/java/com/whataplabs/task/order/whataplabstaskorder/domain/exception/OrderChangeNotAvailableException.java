package com.whataplabs.task.order.whataplabstaskorder.domain.exception;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;

public class OrderChangeNotAvailableException extends OrderBusinessException {
    private final OrderStatus before;
    private final OrderStatus after;

    public OrderChangeNotAvailableException(OrderStatus before, OrderStatus after) {
        super("주문을 변경할 수 없습니다. from " + before + " to " + after);
        this.before = before;
        this.after = after;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.NOT_FOUND_ORDER;
    }
}
