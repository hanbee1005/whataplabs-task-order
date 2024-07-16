package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_product")
public class OrderProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    private Long productId;

    private Integer quantity;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public OrderProduct toDomain() {
        return OrderProduct.builder()
                .orderProductId(id)
                .orderId(order.getId())
                .productId(productId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedAt)
                .build();
    }
}