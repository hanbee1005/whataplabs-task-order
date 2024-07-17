package com.whataplabs.task.order.whataplabstaskorder.domain;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> getOrder(Long orderId);
    List<Order> getOrders();
    Order orderProduct(Order order);
    Order changeOrder(Order order);
    int deleteOrder(Long id);
}
