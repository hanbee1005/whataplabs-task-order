package com.whataplabs.task.order.whataplabstaskorder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER_REQUEST,
    PAY_REQUEST,
    ORDER_COMPLETED,
    ORDER_CANCEL_REQUEST,
    ORDER_CANCELED,
    ;
}
