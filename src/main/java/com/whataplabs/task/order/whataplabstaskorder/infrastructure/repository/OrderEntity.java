package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.Order;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;
import com.whataplabs.task.order.whataplabstaskorder.domain.OrderStatus;
import com.whataplabs.task.order.whataplabstaskorder.domain.exception.OrderChangeNotAvailableException;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductEntity> orderProducts = new ArrayList<>();

    public static OrderEntity create(Order order) {
        OrderEntity entity = new OrderEntity();

        entity.status = order.getStatus();
        entity.totalPrice = order.getTotalPrice();
        entity.createdAt = LocalDateTime.now();
        order.getOrderProducts().stream()
                .map(OrderProductEntity::create)
                .forEach(entity::addProduct);

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

    public void addProduct(OrderProductEntity product) {
        orderProducts.add(product);
        product.setOrder(this);
    }

    public void removeProduct(OrderProductEntity product) {
        orderProducts.remove(product);
        product.setOrder(null);
    }

    public void cancel() {
        changeStatus(OrderStatus.ORDER_CANCELED);
        lastModifiedAt = LocalDateTime.now();
    }

    public void updateStatus(OrderStatus status) {
        changeStatus(status);
        lastModifiedAt = LocalDateTime.now();
    }

    public void changeStatus(OrderStatus status) {
        if (!OrderStatus.changeAvailable(this.status, status)) {
            throw new OrderChangeNotAvailableException(this.status, status);
        }

        this.status = status;
    }

    public void changeOrderProducts(List<OrderProduct> updatedProducts) {
        // Remove items not in the updated list
        List<Long> updatedProductIds = updatedProducts.stream().map(OrderProduct::getProductId).toList();
        orderProducts.stream()
                .filter(op -> !updatedProductIds.contains(op.getProductId()))
                .toList()
                .forEach(this::removeProduct);

        // Update existing items or add new ones
        for (OrderProduct updatedProduct : updatedProducts) {
            Optional<OrderProductEntity> exist = orderProducts.stream()
                    .filter(op -> op.isSameProduct(updatedProduct))
                    .findFirst();

            if (exist.isPresent()) {
                OrderProductEntity existingProduct = exist.get();
                existingProduct.update(updatedProduct.getQuantity(), updatedProduct.getUnitPrice());
            } else {
                addProduct(OrderProductEntity.create(updatedProduct));
            }
        }
    }
}
