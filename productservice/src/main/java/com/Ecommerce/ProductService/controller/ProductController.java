package com.Ecommerce.ProductService.controller;

import com.Ecommerce.ProductService.entity.Product;
import com.Ecommerce.ProductService.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product){

        return service.addProduct(product);
    }

    @GetMapping
    public Page<Product> getAllProducts(@PageableDefault(size = 10,sort = "price") Pageable pageable)
    {
        return service.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id){
        return service.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
    }

    @PutMapping("{id}/reduceStock")
    public void reduceStock(@PathVariable Long id , @RequestParam Integer quantity){
        service.reduceStock(id, quantity);
    }

}
