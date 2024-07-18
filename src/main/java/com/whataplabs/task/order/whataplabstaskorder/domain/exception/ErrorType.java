package com.whataplabs.task.order.whataplabstaskorder.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    INTERNAL_SERVER_ERROR(500001, "INTERNAL_SERVER_ERROR", "알 수 없는 서버 에러가 발생하였습니다."),

    BAD_REQUEST(400001, "BAD_REQUEST", "잘못된 요청 파라미터입니다."),

    NOT_FOUND_ORDER(400201, "NOT_FOUND_ORDER", "존재하지 않는 주문입니다."),
    ORDER_FAIL(400203, "ORDER_FAIL", "주문에 실패하였습니다."),
    ;

    private final int status;
    private final String code;
    private final String defaultMessage;
}
