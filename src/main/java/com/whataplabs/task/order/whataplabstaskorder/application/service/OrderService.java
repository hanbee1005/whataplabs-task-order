package com.whataplabs.task.order.whataplabstaskorder.application.service;

import com.whataplabs.task.order.whataplabstaskorder.domain.*;
import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus.ORDER_CANCEL_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;

    private final ApplicationEventPublisher publisher;

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
        return repository.getOrder(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        return repository.getOrders();
    }

    @Transactional
    public Order orderProducts(Order newOrder) {
        Order order = repository.orderProducts(newOrder);
        publisher.publishEvent(new OrderRequested(order));
        return order;
    }

    @Transactional(readOnly = true)
    public Order changeOrder(Order changeOrder) {
        Order originOrder = getOrder(changeOrder.getId());
        originOrder.checkCanChange(changeOrder.getStatus());

        publisher.publishEvent(OrderChangeRequested.of(originOrder, changeOrder));
        return changeOrder;
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        updateOrderStatus(orderId, ORDER_CANCEL_REQUEST);

        Order order = getOrder(orderId);
        publisher.publishEvent(new OrderCancelRequested(order));
        return order;
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        int affected = repository.updateOrderStatus(orderId, status);
        if (affected == 0) {
            throw new IllegalStateException("order status update fail. orderId=" + orderId + ", status=" + status);
        }
    }

    @Transactional
    public void updateOrder(Long id, List<OrderProduct> change) {
        // TODO 주문 상품을 하나씩 변경 필요
    }
}
