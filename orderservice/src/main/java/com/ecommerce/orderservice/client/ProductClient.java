package com.ecommerce.orderservice.client;


import com.ecommerce.orderservice.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProduct(@PathVariable Long id);

    @PutMapping("/api/products/{id}/reduceStock")
    void reduceStock(@PathVariable Long id , @RequestParam int quantity);



}
