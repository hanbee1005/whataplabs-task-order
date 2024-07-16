package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    @Override
    public Optional<Order> getOrder(Long orderId) {
        return jpaRepository.findByIdWithOrderProducts(orderId).map(OrderEntity::toDomain);
    }

    @Override
    public List<Order> getOrders() {
        return jpaRepository.findAllWithOrderProducts().stream().map(OrderEntity::toDomain).toList();
    }
}
