package com.ecommerce.orderservice.dto;

public record CartItemResponse(
        Long productId,
        String productName,
        int quantity,
        double priceAtPurchase
) {
}
