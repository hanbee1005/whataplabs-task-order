package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderProductsResponse(
        Long id,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderProductResponse> orderProducts
) {
    public static OrderProductsResponse from(Order order) {
        return new OrderProductsResponse(order.getId(), order.getStatus(), order.getTotalPrice(),
                order.getOrderProducts().stream().map(OrderProductResponse::from).toList());
    }
}
