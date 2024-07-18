package com.whataplabs.task.order.whataplabstaskorder.domain;

import com.whataplabs.task.order.whataplabstaskorder.application.event.common.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public record OrderChangeRequested(
        Long orderId,
        OrderStatus originStatus,
        List<OrderProduct> origin,
        List<OrderProduct> change
) implements DomainEvent {
    public static OrderChangeRequested of(Order originOrder, Order changeOrder) {
        return new OrderChangeRequested(originOrder.getId(), originOrder.getStatus(),
                originOrder.getOrderProducts(), changeOrder.getOrderProducts());
    }

    public Order changeOrder() {
        List<OrderProduct> products = new ArrayList<>();
        for (OrderProduct c : change) {
            origin.stream().filter(o -> o.getProductId().equals(c.getProductId()))
                    .findFirst()
                    .ifPresentOrElse(o -> products.add(c.changeQuantity(o.getQuantity())), () -> products.add(c));
        }
        return Order.builder().id(orderId).orderProducts(products).build();
    }
}
