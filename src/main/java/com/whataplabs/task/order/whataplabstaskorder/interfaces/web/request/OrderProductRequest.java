package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderProductRequest(
        Long productId,
        int quantity,
        BigDecimal unitPrice
) {
    public OrderProduct to() {
        return OrderProduct.builder()
                .productId(productId).quantity(quantity).unitPrice(unitPrice).createdAt(LocalDateTime.now())
                .build();
    }
}
