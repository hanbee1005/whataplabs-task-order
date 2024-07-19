package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record ChangeOrderResponse(
        Long id,
        OrderStatus status,
        BigDecimal totalPrice,
        List<ChangeOrderProductResponse> orderProducts
) {
    public static ChangeOrderResponse from(Order order) {
        return new ChangeOrderResponse(order.getId(), order.getStatus(), order.getTotalPrice(),
                order.getOrderProducts().stream().map(ChangeOrderProductResponse::from).toList());
    }
}
