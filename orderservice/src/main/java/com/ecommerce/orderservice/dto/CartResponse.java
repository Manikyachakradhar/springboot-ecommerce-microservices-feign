package com.ecommerce.orderservice.dto;

import java.util.List;

public record CartResponse(

        Long id,
        String userEmail,
        double totalAmount,
        List<CartItemResponse> cartItemResposneList
) {
}
