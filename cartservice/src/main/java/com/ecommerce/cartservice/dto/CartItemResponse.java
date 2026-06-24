package com.ecommerce.cartservice.dto;

public record CartItemResponse(
        Long productId,
        String productName,
        int quantity,
        double priceAtPurchase
) {
}
