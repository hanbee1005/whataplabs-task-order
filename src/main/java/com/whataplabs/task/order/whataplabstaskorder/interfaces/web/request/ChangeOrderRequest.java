package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;

import java.util.List;

public record ChangeOrderRequest(
        List<OrderProductRequest> orderProduct
) {
    public Order to(Long id) {
        return Order.create(id, orderProduct.stream().map(OrderProductRequest::to).toList());
    }
}
