package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.exception;

import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderBusinessException;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderBusinessException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleProductException(OrderBusinessException ex) {
        log.error("[GlobalExceptionHandler.handleProductException] message={}", ex.getErrorMessage());
        return ResponseEntity.status(400).body(CommonResponse.of(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleException(Exception ex) {
        log.error("[GlobalExceptionHandler.handleException] message={}", ex.getMessage());
        return ResponseEntity.status(500).body(CommonResponse.of(ErrorResponse.createInternalServerError()));
    }
}
