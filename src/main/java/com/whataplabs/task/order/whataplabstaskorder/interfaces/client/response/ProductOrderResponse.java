package com.whataplabs.task.order.whataplabstaskorder.interfaces.client.response;

import java.util.List;

public record ProductOrderResponse(
        List<Long> products
) {
    public static ProductOrderResponse empty() {
        return new ProductOrderResponse(List.of());
    }
}
