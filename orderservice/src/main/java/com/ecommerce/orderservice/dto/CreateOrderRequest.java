package com.ecommerce.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(

        @NotNull
         Long productId,
         @Min(value = 1,
                 message = "Quantity must be greater than 0")
         Integer quantity
) {
}
