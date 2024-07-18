package com.whataplabs.task.order.whataplabstaskorder.application.service;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;
import com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository.OrderEntity;
import com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository.OrderJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus.ORDER_CANCEL_REQUEST;
import static com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus.ORDER_COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService service;

    @Autowired
    OrderJpaRepository jpaRepository;

    List<OrderEntity> orders = new ArrayList<>();

    @BeforeEach
    void init() {
        for (Order order : testOrders()) {
            OrderEntity orderEntity = OrderEntity.create(order);
            jpaRepository.save(orderEntity);
            orders.add(orderEntity);
        }
    }

    @Test
    @DisplayName("주문 취소 요청")
    public void cancelOrder() {
        // given
        Long orderId = orders.get(0).getId();
        Order before = service.getOrder(orderId);

        // when
        Order order = service.cancelOrder(orderId);

        // then
        assertThat(before).isNotNull();
        assertThat(before.getId()).isEqualTo(orderId);
        assertThat(before.getStatus()).isEqualTo(ORDER_COMPLETED);

        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getStatus()).isEqualTo(ORDER_CANCEL_REQUEST);
    }

    private List<Order> testOrders() {
        return List.of(
                Order.builder().status(OrderStatus.ORDER_COMPLETED).totalPrice(BigDecimal.valueOf(3000))
                        .orderProducts(List.of(
                                OrderProduct.builder().productId(101L).quantity(3).unitPrice(BigDecimal.valueOf(1000)).createdAt(LocalDateTime.now()).build(),
                                OrderProduct.builder().productId(102L).quantity(2).unitPrice(BigDecimal.valueOf(2000)).createdAt(LocalDateTime.now()).build()
                        ))
                        .createdAt(LocalDateTime.now()).build(),
                Order.builder().status(OrderStatus.ORDER_COMPLETED).totalPrice(BigDecimal.valueOf(5000))
                        .orderProducts(List.of(
                                OrderProduct.builder().productId(103L).quantity(5).unitPrice(BigDecimal.valueOf(1000)).createdAt(LocalDateTime.now()).build(),
                                OrderProduct.builder().productId(104L).quantity(3).unitPrice(BigDecimal.valueOf(2000)).createdAt(LocalDateTime.now()).build(),
                                OrderProduct.builder().productId(105L).quantity(7).unitPrice(BigDecimal.valueOf(2000)).createdAt(LocalDateTime.now()).build()
                        ))
                        .createdAt(LocalDateTime.now()).build()
        );
    }
}