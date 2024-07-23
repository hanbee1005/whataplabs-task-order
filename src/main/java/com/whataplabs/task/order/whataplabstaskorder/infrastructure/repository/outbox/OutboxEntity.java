package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository.outbox;

import com.whataplabs.task.order.whataplabstaskorder.application.event.common.EventType;
import com.whataplabs.task.order.whataplabstaskorder.domain.outbox.Outbox;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "outbox")
public class OutboxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private EventType eventType;
    private String payload;
    private LocalDateTime createdAt;

    public static OutboxEntity create(Outbox outbox) {
        OutboxEntity outboxEntity = new OutboxEntity();
        outboxEntity.eventType = outbox.getEventType();
        outboxEntity.payload = outbox.getPayload();
        outboxEntity.createdAt = LocalDateTime.now();
        return outboxEntity;
    }

    public Outbox toDomain() {
        return Outbox.builder()
                .id(id)
                .eventType(eventType)
                .payload(payload)
                .createdAt(createdAt)
                .build();
    }
}
