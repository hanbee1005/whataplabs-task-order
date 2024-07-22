package com.whataplabs.task.order.whataplabstaskorder.application.event.common;

public interface DomainEvent {
    EventType getEventType();
    String getPayload();
}
