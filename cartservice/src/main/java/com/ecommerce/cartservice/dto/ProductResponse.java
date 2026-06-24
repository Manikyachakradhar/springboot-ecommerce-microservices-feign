package com.ecommerce.cartservice.dto;

public record ProductResponse(

        Long id,
        String name,
        String description,
        Double price,
        Integer quantity,
        String category

) {
}