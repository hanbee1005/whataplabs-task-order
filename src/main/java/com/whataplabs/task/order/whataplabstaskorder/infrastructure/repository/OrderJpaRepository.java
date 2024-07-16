package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.orderProducts WHERE o.id = :orderId")
    Optional<OrderEntity> findByIdWithOrderProducts(Long orderId);

    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.orderProducts")
    List<OrderEntity> findAllWithOrderProducts();
}
