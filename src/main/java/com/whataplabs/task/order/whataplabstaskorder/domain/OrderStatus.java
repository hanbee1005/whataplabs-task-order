package com.whataplabs.task.order.whataplabstaskorder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER_REQUEST("주문 요청"),   // 최초 주문 요청 (재고 확인 이전)
    PAY_REQUEST("결제 요청"),     // 재고 확인 후 결제 요청
    ORDER_COMPLETED("주문 완료"), // 재고 확인 및 결제까지 완료된 상태

    ORDER_CANCEL_REQUEST("주문 취소 요청"),  // 주문 취소를 요청한 상태 (재고 원복 이전)
    PAY_CANCEL_REQUEST("결제 취소 요청"),    // 재고 원복 후 결제 취소 요청
    ORDER_CANCELED("주문 취소")             // 재고 원복 및 결제 취소까지 완료된 상태
    ;

    private final String desc;
}
