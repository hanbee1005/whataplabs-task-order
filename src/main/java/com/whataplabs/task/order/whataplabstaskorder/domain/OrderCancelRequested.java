package com.whataplabs.task.order.whataplabstaskorder.domain;

import com.whataplabs.task.order.whataplabstaskorder.application.event.common.DomainEvent;

public record OrderCancelRequested(
        Order order
) implements DomainEvent {
}
