package com.whataplabs.task.order.whataplabstaskorder.application.event.listener;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderCancelRequested;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderChangeRequested;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderRequested;
import com.whataplabs.task.order.whataplabstaskorder.domain.outbox.Outbox;
import com.whataplabs.task.order.whataplabstaskorder.domain.outbox.OutboxCreated;
import com.whataplabs.task.order.whataplabstaskorder.domain.outbox.OutboxRepository;
import com.whataplabs.task.order.whataplabstaskorder.infrastructure.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventListener {
    private final OutboxRepository outboxRepository;
    private final ApplicationEventPublisher publisher;

    @Async
    @EventListener({ApplicationReadyEvent.class, OutboxCreated.class})
    public void deliveryOutboxEvents() {
        log.debug("[deliveryOutboxEvents] START {}", LocalDateTime.now());
        while (true) {
            Outbox outbox = outboxRepository.popOutbox();

            if (outbox == null) {
                break;
            }

            switch (outbox.getEventType()) {
                case ORDER_REQUESTED -> publisher.publishEvent(makeEvent(outbox.getPayload(), OrderRequested.class));
                case ORDER_CHANGE_REQUESTED -> publisher.publishEvent(makeEvent(outbox.getPayload(), OrderChangeRequested.class));
                case ORDER_CANCEL_REQUESTED -> publisher.publishEvent(makeEvent(outbox.getPayload(), OrderCancelRequested.class));
            }

            outboxRepository.removeOutbox(outbox);
            log.debug("[deliveryOutboxEvents] END {}", LocalDateTime.now());
        }
    }

    private <T> T makeEvent(String payload, Class<T> clazz) {
        return JsonUtil.fromJson(payload, clazz);
    }

}
