package com.ecommerce.orderservice.dto;

public record OrderItemResponse(
        String productName,
        Integer quantity,
        Double price
) {}
