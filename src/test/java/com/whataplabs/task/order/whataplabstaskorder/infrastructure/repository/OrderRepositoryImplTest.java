package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

}