package com.ecommerce.orderservice.dto;


import com.ecommerce.orderservice.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderHistoryResponse(

        Long orderId,
        LocalDateTime createdAt,
        OrderStatus status,
        Double totalAmount,
        List<OrderItemResponse> orderItems

) {
}
