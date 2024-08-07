package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderRepository;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;
import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrder(Long orderId) {
        return jpaRepository.findByIdWithOrderProducts(orderId).map(OrderEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        return jpaRepository.findAllWithOrderProducts().stream().map(OrderEntity::toDomain).toList();
    }

    @Override
    @Transactional
    public Order orderProducts(Order order) {
        OrderEntity orderEntity = OrderEntity.create(order);
        jpaRepository.save(orderEntity);
        return orderEntity.toDomain();
    }

    @Override
    @Transactional
    public Order changeOrder(Order order) {
        OrderEntity orderEntity = jpaRepository.findByIdWithOrderProducts(order.getId())
                .orElseThrow(() -> new OrderNotFoundException(order.getId()));

        orderEntity.updateStatus(order.getStatus());
        orderEntity.changeOrderProducts(order.getOrderProducts());

        jpaRepository.save(orderEntity);
        return orderEntity.toDomain();
    }

    @Override
    @Transactional
    public int updateOrderStatus(Long id, OrderStatus status) {
        OrderEntity orderEntity = jpaRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        orderEntity.updateStatus(status);
        return 1;
    }
}
