package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, Long> {
    Optional<OutboxEntity> findFirstByOrderByIdAsc();
}
