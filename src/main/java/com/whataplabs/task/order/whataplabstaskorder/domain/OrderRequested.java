package com.whataplabs.task.order.whataplabstaskorder.domain;

import com.whataplabs.task.order.whataplabstaskorder.application.event.common.DomainEvent;
import com.whataplabs.task.order.whataplabstaskorder.application.event.common.EventType;
import com.whataplabs.task.order.whataplabstaskorder.infrastructure.util.JsonUtil;

public record OrderRequested(
        Order order
) implements DomainEvent {
    @Override
    public EventType getEventType() {
        return EventType.ORDER_REQUESTED;
    }

    @Override
    public String getPayload() {
        return "{\"order\":" + JsonUtil.toJson(order) + "}";
    }
}
