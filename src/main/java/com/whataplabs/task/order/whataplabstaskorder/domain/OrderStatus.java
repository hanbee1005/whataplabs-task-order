package com.whataplabs.task.order.whataplabstaskorder.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER_REQUEST("주문 요청"),   // 주문 요청 (재고 확인 이전)
    ORDER_COMPLETED("주문 완료"), // 재고 확인이 완료된 상태
    ORDER_FAILED("주문 실패"),    // 재고가 부족해서 주문이 불가능한 상태

    ORDER_CANCEL_REQUEST("주문 취소 요청"),  // 주문 취소를 요청한 상태 (재고 원복 이전)
    ORDER_CANCELED("주문 취소")             // 재고 원복이 완료된 상태
    ;

    private final String desc;

    public static boolean changeAvailable(OrderStatus before, OrderStatus after) {
        return switch (after) {
            case ORDER_REQUEST -> List.of(ORDER_COMPLETED, ORDER_FAILED).contains(before);
            case ORDER_COMPLETED, ORDER_FAILED -> before == ORDER_REQUEST;
            case ORDER_CANCEL_REQUEST -> before == ORDER_COMPLETED;
            case ORDER_CANCELED -> before == ORDER_CANCEL_REQUEST;
        };
    }
}
