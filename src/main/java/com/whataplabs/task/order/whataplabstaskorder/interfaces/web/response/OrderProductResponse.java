package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;

import java.math.BigDecimal;

public record OrderProductResponse(
        Long orderProductId,
        Long orderId,
        Long productId,
        int quantity,
        BigDecimal unitPrice
) {
    public static OrderProductResponse from(OrderProduct op) {
        return new OrderProductResponse(op.getOrderProductId(), op.getOrderId(), op.getProductId(), op.getQuantity(), op.getUnitPrice());
    }
}
