package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.dto.AddToCartRequest;
import com.ecommerce.cartservice.dto.CartResponse;
import com.ecommerce.cartservice.dto.UpdateCartItemRequest;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @PostMapping("/add")
    public  String addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest){
        return cartService.addToCart(addToCartRequest);

    }

    @GetMapping("/{userEmail}")
    public CartResponse getCart(@PathVariable String userEmail){
        return cartService.getCart(userEmail);


    }
    @DeleteMapping("/{userEmail}/products/{productId}")
    public String deleteFromCart(@PathVariable String userEmail , @PathVariable  Long productId){

        cartService.deleteProductFromCarr(userEmail,productId);
        return "Product Removed from cart";

    }
    @PutMapping("/update")
    public String updateCart(@Valid @RequestBody UpdateCartItemRequest updateCartItemRequest){


        cartService.updateCart(updateCartItemRequest);
        return "Product Updated Successfully";
    }
    @DeleteMapping("/{userEmail}")
    public String clearCart(@PathVariable String userEmail){

        cartService.clearCart(userEmail);

        return  "Cart Cleared successfully";
    }





}
