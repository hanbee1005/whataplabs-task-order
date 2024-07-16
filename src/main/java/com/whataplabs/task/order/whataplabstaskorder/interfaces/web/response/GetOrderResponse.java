package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record GetOrderResponse(
        Long orderId,
        OrderStatus status,
        BigDecimal totalPrice,
        List<GetOrderProductResponse> orderProducts
) {
    public static GetOrderResponse from(Order order) {
        return new GetOrderResponse(order.getId(), order.getStatus(), order.getTotalPrice(),
                order.getOrderProducts().stream().map(GetOrderProductResponse::from).toList());
    }
}
