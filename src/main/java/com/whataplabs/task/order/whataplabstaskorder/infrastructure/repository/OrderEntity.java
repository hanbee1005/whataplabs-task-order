package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;
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

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    public static OrderEntity create(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.status = order.getStatus();
        entity.totalPrice = order.getTotalPrice();
        entity.createdAt = LocalDateTime.now();
        order.getOrderProducts().forEach(entity::addProduct);
        return entity;
    }

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

    public void addProduct(OrderProduct product) {
        OrderProductEntity op = OrderProductEntity.create(product);
        orderProducts.add(op);
        op.setOrder(this);
    }

    public void requestCancel() {
        changeStatus(OrderStatus.ORDER_CANCEL_REQUEST);
    }

    public void changeStatus(OrderStatus status) {
        if (!OrderStatus.changeAvailable(this.status, status)) {
            throw new IllegalArgumentException("해당 상태로 변경이 불가합니다. before=" + this.status + ", after=" + status);
        }

        this.status = status;
    }
}
