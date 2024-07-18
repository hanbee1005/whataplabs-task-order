package com.whataplabs.task.order.whataplabstaskorder.interfaces.client.request;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;

import java.util.List;

public record ProductOrderRequest(
        Long orderId,
        List<OrderedProductRequest> orderProducts
) {
    public static ProductOrderRequest from(Order order) {
        return new ProductOrderRequest(order.getId(),
                order.getOrderProducts().stream().map(OrderedProductRequest::from).toList());
    }
}
