package com.ecommerce.orderservice.dto;

public record ProductResponse(
        Long id,
        String name,
        double price,
        Integer quantity
) {
}
