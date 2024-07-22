package com.whataplabs.task.order.whataplabstaskorder.domain.outbox;

import com.whataplabs.task.order.whataplabstaskorder.application.event.common.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventPublisher {
    private final ApplicationEventPublisher publisher;
    private final OutboxRepository repository;

    @Transactional
    public void publish(DomainEvent event) {
        Outbox outbox = Outbox.builder()
                .eventType(event.getEventType())
                .payload(event.getPayload())
                .createdAt(LocalDateTime.now())
                .build();

        repository.pushOutbox(outbox);
        publisher.publishEvent(new OutboxCreated());
    }
}
