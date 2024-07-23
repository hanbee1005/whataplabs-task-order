package com.whataplabs.task.order.whataplabstaskorder.domain.outbox;

import com.whataplabs.task.order.whataplabstaskorder.application.event.common.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Outbox {
    private Long id;
    private EventType eventType;
    private String payload;
    private LocalDateTime createdAt;
}
