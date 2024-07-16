package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
}
