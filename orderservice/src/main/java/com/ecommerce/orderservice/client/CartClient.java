package com.ecommerce.orderservice.client;


import com.ecommerce.orderservice.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service")
public interface CartClient {

    @GetMapping("/api/cart/{userEmail}")
    CartResponse getCart(@PathVariable String userEmail);


    @DeleteMapping("/api/cart/{userEmail}")
    void deleteCart(@PathVariable String userEmail);




}
