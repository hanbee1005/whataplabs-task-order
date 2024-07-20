package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;

import javax.validation.Valid;
import java.util.List;

public record OrderProductsRequest(
        List<@Valid OrderProductRequest> orderProduct
) {
    public Order to() {
        return Order.create(orderProduct.stream().map(OrderProductRequest::to).toList());
    }
}
