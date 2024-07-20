package com.whataplabs.task.order.whataplabstaskorder.interfaces.web;

import com.whataplabs.task.order.whataplabstaskorder.application.service.OrderService;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request.ChangeOrderRequest;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request.OrderProductsRequest;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.web.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Tag(name = "주문 API", description = "주문 조회, 요청, 변경, 취소 API")
@RestController
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService service;

    @Operation(summary = "주문 조회 (by. id)", description = "id로 주문 id, 주문 상태, 주문 상품 목록을 조회합니다.")
    @GetMapping("/orders/{id}")
    public ResponseEntity<CommonResponse<GetOrderResponse>> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.ok(GetOrderResponse.from(service.getOrder(id))));
    }

    @Operation(summary = "주문 목록 조회", description = "전체 주문 목록을 조회합니다. (별도 paging 적용 x)")
    @GetMapping("/orders")
    public ResponseEntity<CommonResponse<GetOrdersResponse>> getOrders() {
        return ResponseEntity.ok(CommonResponse.ok(GetOrdersResponse.from(service.getOrders())));
    }

    @Operation(summary = "주문 요청", description = "주문 상폼 목록을 받아 주문을 요청합니다.")
    @PostMapping("/orders")
    public ResponseEntity<CommonResponse<OrderProductsResponse>> orderProduct(@RequestBody @Valid OrderProductsRequest request) {
        return ResponseEntity.ok(CommonResponse.ok(OrderProductsResponse.from(service.orderProducts(request.to()))));
    }

    @Operation(summary = "주문 변경 요청", description = "변경할 주문 상품 목록을 받아 id에 해당하는 주문을 변경 요청합니다.")
    @PutMapping("/orders/{id}")
    public ResponseEntity<CommonResponse<ChangeOrderResponse>> changeOrder(@PathVariable Long id,
                                                                           @RequestBody @Valid ChangeOrderRequest request) {
        return ResponseEntity.ok(CommonResponse.ok(ChangeOrderResponse.from(service.changeOrder(request.to(id)))));
    }

    @Operation(summary = "주문 취소 요청", description = "id에 해당하는 주문을 취소 요청합니다.")
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<CommonResponse<OrderProductsResponse>> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.ok(OrderProductsResponse.from(service.cancelOrder(id))));
    }
}
