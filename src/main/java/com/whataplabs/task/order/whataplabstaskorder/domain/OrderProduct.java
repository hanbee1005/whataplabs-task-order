package com.whataplabs.task.order.whataplabstaskorder.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderProduct {
    private Long orderProductId;
    private Long orderId;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public BigDecimal getOrderPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public OrderProduct changeQuantity(int before) {
        return OrderProduct.builder()
                .orderProductId(orderProductId)
                .orderId(orderId)
                .productId(productId)
                .quantity(quantity - before)
                .unitPrice(unitPrice)
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedAt)
                .build();
    }
}
