package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository.outbox;

import com.whataplabs.task.order.whataplabstaskorder.application.event.common.EventType;
import com.whataplabs.task.order.whataplabstaskorder.domain.outbox.Outbox;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_history")
public class OutboxHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long outboxId;
    @Enumerated(value = EnumType.STRING)
    private EventType eventType;
    private String payload;

    private LocalDateTime createdAt;

    public static OutboxHistoryEntity create(Outbox outbox) {
        OutboxHistoryEntity entity = new OutboxHistoryEntity();
        entity.outboxId = outbox.getId();
        entity.eventType = outbox.getEventType();
        entity.payload = outbox.getPayload();
        entity.createdAt = LocalDateTime.now();
        return entity;
    }
}
