package com.whataplabs.task.order.whataplabstaskorder.interfaces.web.request;

import com.whataplabs.task.order.whataplabstaskorder.domain.OrderProduct;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderProductRequest(
        @Schema(description = "상품 id", example = "101")
        @NotNull
        Long productId,
        @Schema(description = "주문 수량", example = "3")
        @PositiveOrZero
        int quantity,
        @Schema(description = "주문 상품 가격", example = "1000")
        @NotNull
        @PositiveOrZero
        BigDecimal unitPrice
) {
    public OrderProduct to() {
        return OrderProduct.builder()
                .productId(productId).quantity(quantity).unitPrice(unitPrice).createdAt(LocalDateTime.now())
                .build();
    }
}
