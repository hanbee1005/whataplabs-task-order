package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;

import java.util.List;

public record GetOrdersResponse(
        List<GetOrderResponse> orders
) {
    public static GetOrdersResponse from(List<Order> orders) {
        return new GetOrdersResponse(orders.stream().map(GetOrderResponse::from).toList());
    }
}
