package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus.ORDER_CANCEL_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderRepositoryImplTest {

    @Autowired OrderRepositoryImpl repository;

    @Test
    @DisplayName("id 로 주문 조회")
    public void getOrder() {
        // given
        Long orderId = 101L;

        // when
        Order order = repository.getOrder(orderId).orElse(null);

        // then
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("주문 목록 조회")
    public void getOrders() {
        // given
        // when
        List<Order> orders = repository.getOrders();

        // then
        assertThat(orders).isNotEmpty();
    }

    @Test
    @DisplayName("주문 생성을 요청하면 ORDER_REQUEST 상태로 저장됩니다.")
    public void addOrder() {
        // given
        Order order = Order.create(List.of(
                OrderProduct.builder().productId(101L).quantity(5).unitPrice(BigDecimal.valueOf(5000)).build(),
                OrderProduct.builder().productId(102L).quantity(2).unitPrice(BigDecimal.valueOf(1000)).build(),
                OrderProduct.builder().productId(103L).quantity(3).unitPrice(BigDecimal.valueOf(1200)).build()
        ));

        // when
        Order ordered = repository.orderProducts(order);

        // then
        assertThat(ordered).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER_REQUEST);
        assertThat(order.getTotalPrice().intValue()).isEqualTo(30600);
        assertThat(order.getOrderProducts().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("주문 취소를 요청하면 ORDER_CANCEL_REQUEST 상태로 변경됩니다.")
    public void deleteOrder() {
        // given
        Long orderId = 102L;

        // when
        int affected = repository.updateOrderStatus(orderId, ORDER_CANCEL_REQUEST);
        assertThat(affected).isEqualTo(1);

        Order cancelRequest = repository.getOrder(orderId).orElse(null);

        // then
        assertThat(cancelRequest).isNotNull();
        assertThat(cancelRequest.getStatus()).isEqualTo(ORDER_CANCEL_REQUEST);
    }
}