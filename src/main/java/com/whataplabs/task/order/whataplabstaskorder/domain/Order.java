package com.whataplabs.task.order.whataplabstaskorder.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Order {
    private Long id;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private List<OrderProduct> orderProducts;

    public static Order create(List<OrderProduct> orderProducts) {
        return Order.builder()
                .status(OrderStatus.ORDER_REQUEST)
                .createdAt(LocalDateTime.now())
                .totalPrice(calculateTotalPrice(orderProducts))
                .orderProducts(orderProducts)
                .build();
    }

    private static BigDecimal calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(OrderProduct::getOrderPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
