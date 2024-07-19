package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;

import java.math.BigDecimal;

public record ChangeOrderProductResponse(
        Long productId,
        int quantity,
        BigDecimal unitPrice
) {
    public static ChangeOrderProductResponse from(OrderProduct op) {
        return new ChangeOrderProductResponse(op.getProductId(), op.getQuantity(), op.getUnitPrice());
    }
}
