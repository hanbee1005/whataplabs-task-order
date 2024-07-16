package com.whataplabs.task.order.whataplabstaskorder.interfaces.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whataplabs.task.order.whataplabstaskorder.application.service.OrderService;
import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService service;

    @Test
    @DisplayName("id로 주문 조회")
    public void getOrder() throws Exception {
        // given
        Long id = 1L;
        Order order = Order.builder()
                .id(id).status(OrderStatus.ORDER_REQUEST).totalPrice(BigDecimal.valueOf(10000))
                .orderProducts(List.of(
                        OrderProduct.builder().orderProductId(1L).orderId(1L).productId(1L).quantity(1).unitPrice(BigDecimal.valueOf(5000)).build(),
                        OrderProduct.builder().orderProductId(2L).orderId(1L).productId(2L).quantity(2).unitPrice(BigDecimal.valueOf(2500)).build()
                ))
                .build();
        given(service.getOrder(anyLong())).willReturn(order);

        // when
        // then
        mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").value(id))
                .andExpect(jsonPath("$.data.status").value(OrderStatus.ORDER_REQUEST.name()))
                .andExpect(jsonPath("$.data.totalPrice").value(10000))
        ;
    }

    @Test
    @DisplayName("주문 목록 조회")
    public void getOrders() throws Exception {
        // given

        // when
        // then
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

}