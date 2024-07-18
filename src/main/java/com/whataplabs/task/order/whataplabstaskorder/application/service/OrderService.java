package com.whataplabs.task.order.whataplabstaskorder.application.service;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderRepository;
import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderNotFoundException;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.client.ProductClient;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.client.request.ProductOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final ProductClient productClient;

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

        // TODO 이벤트 발행 - 주문 요청 이벤트
        try {
            productClient.checkStockAndDeduct(ProductOrderRequest.from(order));
        } catch (Exception e) {
            log.error("[OrderService.orderProducts] {}", e.getMessage());
        }
        // TODO 주문 상태 업데이트 필요

        return order;
    }

    @Transactional
    public Order changeOrder(Order chageOrder) {
        Order order = repository.changeOrder(chageOrder);
        // TODO 이벤트 발행 - 주문 요청 이벤트
        return order;
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        int affected = repository.deleteOrder(orderId);
        if (affected == 0) {
            throw new IllegalStateException("order delete fail");
        }
        // TODO 이벤트 발행 - 주문 취소 요청 이벤트
    }
}
