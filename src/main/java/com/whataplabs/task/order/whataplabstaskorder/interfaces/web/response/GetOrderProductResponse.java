package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;

import java.math.BigDecimal;

public record GetOrderProductResponse(
        Long orderProductId,
        Long orderId,
        Long productId,
        int quantity,
        BigDecimal unitPrice
) {
    public static GetOrderProductResponse from(OrderProduct orderProduct) {
        return new GetOrderProductResponse(orderProduct.getOrderProductId(), orderProduct.getOrderId(), orderProduct.getProductId(),
                orderProduct.getQuantity(), orderProduct.getUnitPrice());
    }
}
