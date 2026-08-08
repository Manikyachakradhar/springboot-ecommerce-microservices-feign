package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.AddToCartRequest;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart APIs", description = "Shopping Cart Management APIs")
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Add Product To Cart",
            description = "Adds a product to the authenticated user's shopping cart."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or insufficient stock"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })

    @PostMapping("/add")
    public  String addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest){
        return cartService.addToCart(addToCartRequest);

    }

    @Operation(
            summary = "Get User Cart",
            description = "Returns all products available in the authenticated user's cart."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();


    }
    @Operation(
            summary = "Remove Product From Cart",
            description = "Removes a specific product from the authenticated user's cart."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product removed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Product not found in cart")
    })
    @DeleteMapping("/products/{productId}")
    public String deleteFromCart( @PathVariable  Long productId){

        cartService.deleteProductFromCarr(productId);
        return "Product Removed from cart";

    }
    @Operation(
            summary = "Update Cart Item",
            description = "Updates the quantity of an existing cart item."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid quantity"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Product not found in cart")
    })
    @PutMapping("/update")
    public String updateCart(@Valid @RequestBody UpdateCartItemRequest updateCartItemRequest){


        cartService.updateCart(updateCartItemRequest);
        return "Product Updated Successfully";
    }
    @Operation(
            summary = "Clear Cart",
            description = "Removes all products from the authenticated user's cart."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping
    public String clearCart(){

        cartService.clearCart();

        return  "Cart Cleared successfully";
    }





}
