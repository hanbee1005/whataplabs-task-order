package com.whataplabs.task.order.whataplabstaskorder.domain;

import com.whataplabs.task.order.whataplabstaskorder.application.event.common.DomainEvent;

public record OrderRequested(
        Order order
) implements DomainEvent {
}
