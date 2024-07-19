package com.whataplabs.task.order.whataplabstaskorder.infrastructure.repository;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Getter
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

    public static OrderProductEntity create(OrderProduct orderProduct) {
        OrderProductEntity entity = new OrderProductEntity();
        entity.productId = orderProduct.getProductId();
        entity.quantity = orderProduct.getQuantity();
        entity.unitPrice = orderProduct.getUnitPrice();
        entity.createdAt = LocalDateTime.now();
        return entity;
    }

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

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public boolean isSameProduct(OrderProduct other) {
        return productId.equals(other.getProductId());
    }

    public void updateQuantity(int quantity) {
        if (quantity < 0) {
            log.error("[OrderProductEntity.updateQuantity] quantity invalid {}", unitPrice);
            throw new IllegalArgumentException("주문 상품의 수량은 음수일 수 없습니다.");
        }

        this.quantity = quantity;
    }

    public void updateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            log.error("[OrderProductEntity.updateUnitPrice] price invalid {}", unitPrice);
            throw new IllegalArgumentException("유효하지 않은 가격입니다. price=" + unitPrice);
        }

        this.unitPrice = unitPrice;
    }
}
