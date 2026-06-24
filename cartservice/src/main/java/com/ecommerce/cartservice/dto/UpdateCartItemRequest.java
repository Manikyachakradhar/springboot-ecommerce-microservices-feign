package com.ecommerce.cartservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record UpdateCartItemRequest(

            @NotNull
            String userEmail,
            @NotNull
            Long productId,

            @Min(value = 1 ,message = "Quantity should be greater than 0")
            int quantity
) {
}
