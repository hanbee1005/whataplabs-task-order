package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    public Order toDomain() {
        return Order.builder()
                .id(id)
                .status(status)
                .totalPrice(totalPrice)
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedAt)
                .orderProducts(orderProducts.stream().map(OrderProductEntity::toDomain).toList())
                .build();
    }
}