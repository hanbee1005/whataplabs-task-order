package com.whataplabs.task.order.whataplabstaskorder.interfaces.client.request;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;

import java.math.BigDecimal;

public record OrderedProductRequest(
        Long productId,
        int quantity,
        BigDecimal unitPrice
) {
    public static OrderedProductRequest from(OrderProduct orderProduct) {
        return new OrderedProductRequest(orderProduct.getProductId(), orderProduct.getQuantity(), orderProduct.getUnitPrice());
    }
}
