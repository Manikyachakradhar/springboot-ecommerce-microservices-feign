package com.ecommerce.cartservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(

        @NotNull
        Long productId,
        String userEmail,
        @Min(value = 1 ,message = "Quantity must be greater than 0")
        Integer quantity

) {
}
