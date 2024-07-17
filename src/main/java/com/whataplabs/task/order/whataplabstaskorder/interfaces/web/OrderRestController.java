package com.whataplabs.task.order.whataplabstaskorder.interfaces.web;

import com.whataplabs.task.order.whataplabstaskorder.application.service.OrderService;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request.ChangeOrderRequest;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request.OrderProductsRequest;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.CommonResponse;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.GetOrderResponse;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.GetOrdersResponse;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.OrderProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping("/orders")
    public ResponseEntity<CommonResponse<OrderProductsResponse>> orderProduct(@RequestBody @Valid OrderProductsRequest request) {
        return ResponseEntity.ok(CommonResponse.ok(OrderProductsResponse.from(service.orderProducts(request.to()))));
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<CommonResponse<Object>> changeOrder(@PathVariable Long id,
                                                              @RequestBody @Valid ChangeOrderRequest request) {
        service.changeOrder(request.to());
        return ResponseEntity.ok(CommonResponse.ok(id));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<CommonResponse<Object>> deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
        return ResponseEntity.ok(CommonResponse.ok(id));
    }
}
