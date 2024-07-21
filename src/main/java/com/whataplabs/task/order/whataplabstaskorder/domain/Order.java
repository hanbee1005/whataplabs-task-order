package com.whataplabs.task.order.whataplabstaskorder.domain;

import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderChangeNotAvailableException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus.ORDER_REQUEST;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    private Long id;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private List<OrderProduct> orderProducts;

    @Builder
    public Order(Long id, OrderStatus status, BigDecimal totalPrice, LocalDateTime createdAt, LocalDateTime lastModifiedAt, List<OrderProduct> orderProducts) {
        this.id = id;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.orderProducts = orderProducts;
    }

    public static Order create(List<OrderProduct> orderProducts) {
        return Order.builder()
                .status(ORDER_REQUEST)
                .createdAt(LocalDateTime.now())
                .totalPrice(calculateTotalPrice(orderProducts))
                .orderProducts(orderProducts)
                .build();
    }

    public static Order create(Long id, List<OrderProduct> orderProducts) {
        return Order.builder()
                .id(id)
                .status(ORDER_REQUEST)
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

    public void checkCanChange(Order change) {
        boolean available = OrderStatus.changeAvailable(status, change.status);
        if (!available) {
            throw new OrderChangeNotAvailableException(status, change.status);
        }
    }
}
