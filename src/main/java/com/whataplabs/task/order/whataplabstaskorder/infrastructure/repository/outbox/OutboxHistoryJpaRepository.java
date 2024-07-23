package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxHistoryJpaRepository extends JpaRepository<OutboxHistoryEntity, Long> {
}
