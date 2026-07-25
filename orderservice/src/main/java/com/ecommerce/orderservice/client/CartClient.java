package com.ecommerce.orderservice.client;


import com.ecommerce.orderservice.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service")
public interface CartClient {

    @GetMapping("/api/cart")
    CartResponse getCart();


    @DeleteMapping("/api/cart")
    void deleteCart();




}
