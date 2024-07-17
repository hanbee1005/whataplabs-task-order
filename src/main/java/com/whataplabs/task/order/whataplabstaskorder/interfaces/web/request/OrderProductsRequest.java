package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;

import java.util.List;

public record OrderProductsRequest(
        List<OrderProductRequest> orderProduct
) {
    public Order to() {
        return Order.create(orderProduct.stream().map(OrderProductRequest::to).toList());
    }
}
