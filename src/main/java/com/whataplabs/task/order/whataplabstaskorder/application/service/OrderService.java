package com.whataplabs.task.order.whataplabstaskorder.application.service;

import com.whataplabs.task.order.whataplabstaskorder.domain.*;
import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderNotFoundException;
import com.whataplabs.task.order.whataplabstaskorder.domain.outbox.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus.ORDER_CANCEL_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;

    private final DomainEventPublisher publisher;

    public Order getOrder(Long orderId) {
        return repository.getOrder(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<Order> getOrders() {
        return repository.getOrders();
    }

    public Order orderProducts(Order newOrder) {
        Order order = repository.orderProducts(newOrder);
        publisher.publish(new OrderRequested(order));
        return order;
    }

    public Order changeOrder(Order changeOrder) {
        Order originOrder = getOrder(changeOrder.getId());
        originOrder.checkCanChange(changeOrder);
        updateOrderStatus(originOrder.getId(), changeOrder.getStatus());

        publisher.publish(OrderChangeRequested.of(originOrder, changeOrder));
        return changeOrder;
    }

    public Order cancelOrder(Long orderId) {
        updateOrderStatus(orderId, ORDER_CANCEL_REQUEST);

        Order order = getOrder(orderId);
        publisher.publish(new OrderCancelRequested(order));
        return order;
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        int affected = repository.updateOrderStatus(orderId, status);
        if (affected == 0) {
            throw new IllegalStateException("order status update fail. orderId=" + orderId + ", status=" + status);
        }
    }

    public void updateOrder(Long id, OrderStatus status, List<OrderProduct> change) {
        Order order = repository.changeOrder(Order.builder()
                .id(id)
                .status(status)
                .orderProducts(change)
                .build());

        log.info("[OrderService.updateOrder] order updated success. id={}", order.getId());
    }
}
