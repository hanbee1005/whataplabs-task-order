package com.whataplabs.task.order.whataplabstaskorder.interfaces.web;

import com.whataplabs.task.order.whataplabstaskorder.application.service.OrderService;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.CommonResponse;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.GetOrderResponse;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.GetOrdersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService service;

    @GetMapping("/orders/{id}")
    public ResponseEntity<CommonResponse<GetOrderResponse>> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.ok(GetOrderResponse.from(service.getOrder(id))));
    }

    @GetMapping("/orders")
    public ResponseEntity<CommonResponse<GetOrdersResponse>> getOrders() {
        return ResponseEntity.ok(CommonResponse.ok(GetOrdersResponse.from(service.getOrders())));
    }
}
