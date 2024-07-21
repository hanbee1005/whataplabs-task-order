package com.whataplabs.task.order.whataplabstaskorder.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
    private Long orderProductId;
    private Long orderId;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    @Builder
    public OrderProduct(Long orderProductId, Long orderId, Long productId, int quantity, BigDecimal unitPrice, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.orderProductId = orderProductId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

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
