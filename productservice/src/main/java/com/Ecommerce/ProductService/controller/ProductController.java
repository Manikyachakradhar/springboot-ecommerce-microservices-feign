package com.Ecommerce.ProductService.controller;

import com.Ecommerce.ProductService.entity.Product;
import com.Ecommerce.ProductService.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product APIs", description = "Product Management APIs")
public class ProductController {
    private final ProductService service;

    @Operation(
            summary = "Create Product",
            description = "Creates a new product. Accessible only to ADMIN users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product details"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    public Product addProduct(@Valid @RequestBody Product product){

        return service.addProduct(product);
    }

    @Operation(
            summary = "Get All Products",
            description = "Returns a paginated list of all available products."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public Page<Product> getAllProducts(@ParameterObject  @PageableDefault(size = 10,sort = "price") Pageable pageable)
    {
        return service.getAllProducts(pageable);
    }

    @Operation(
            summary = "Get Product By Id",
            description = "Returns product details using product id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product Found"),
            @ApiResponse(responseCode = "404", description = "Product Not Found")
    })
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id){
        return service.getProductById(id);
    }

    @Operation(
            summary = "Delete Product",
            description = "Deletes a product using its ID. Accessible only to ADMIN users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
    }

    @Operation(
            summary = "Reduce Product Stock",
            description = "Reduces product inventory after a successful order."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock reduced successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}/reduceStock")
    public void reduceStock(@PathVariable Long id , @RequestParam Integer quantity){
        service.reduceStock(id, quantity);
    }

    @Operation(
            summary = "Increase Product Stock",
            description = "Restores product inventory when an order is cancelled."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock restored successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{productId}/increaseStock")
    public void increaseStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        service.increaseStock(productId, quantity);
    }

}
