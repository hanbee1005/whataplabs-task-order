package com.whataplabs.task.order.whataplabstaskorder.application.event.listener;

import com.whataplabs.task.order.whataplabstaskorder.application.service.OrderService;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderCancelRequested;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderRequested;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;
import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderFailException;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.client.ProductClient;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.client.request.ProductOrderRequest;
import com.whataplabs.task.order.whataplabstaskorder.interfaces.client.response.ProductOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final ProductClient productClient;
    private final OrderService orderService;

    @Async
    @EventListener
    public void orderRequested(OrderRequested event) {
        log.info("[OrderEventListener.orderRequested] START order request. orderId={}", event.order().getId());

        try {
            ProductOrderResponse productOrderResponse = productClient.checkStockAndDeduct(ProductOrderRequest.from(event.order()));
            log.info("stock check and deduct succeed productIds{}", productOrderResponse.products());
            orderService.updateOrderStatus(event.order().getId(), OrderStatus.ORDER_COMPLETED);
        } catch (OrderFailException e) {
            log.error("[OrderService.orderProducts] orderId={}, message={}", e.getOrderId(), e.getExternalErrorMessage());
            orderService.updateOrderStatus(event.order().getId(), OrderStatus.ORDER_FAILED);
        }

        log.info("[OrderEventListener.orderRequested] END order request. orderId={}", event.order().getId());
    }

    @Async
    @EventListener
    public void orderCancelRequested(OrderCancelRequested event) {
        log.info("[OrderEventListener.orderCancelRequested] START order cancel request. orderId={}", event.order().getId());

        try {
            ProductOrderResponse productOrderResponse = productClient.checkStockAndRestock(ProductOrderRequest.from(event.order()));
            log.info("stock check and restock succeed productIds{}", productOrderResponse.products());
            orderService.updateOrderStatus(event.order().getId(), OrderStatus.ORDER_CANCELED);
        } catch (OrderFailException e) {
            log.error("[OrderService.orderCancelRequested] orderId={}, message={}", e.getOrderId(), e.getExternalErrorMessage());
        }

        log.info("[OrderEventListener.orderCancelRequested] END order cancel request. orderId={}", event.order().getId());
    }
}
