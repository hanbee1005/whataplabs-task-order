package com.whataplabs.task.order.whataplabstaskorder.domain.outbox;

public interface OutboxRepository {
    void pushOutbox(Outbox outbox);
    Outbox popOutbox();
    void removeOutbox(Outbox outbox);
}
