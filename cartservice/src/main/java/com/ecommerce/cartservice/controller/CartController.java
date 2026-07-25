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

    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();


    }
    @DeleteMapping("/products/{productId}")
    public String deleteFromCart( @PathVariable  Long productId){

        cartService.deleteProductFromCarr(productId);
        return "Product Removed from cart";

    }
    @PutMapping("/update")
    public String updateCart(@Valid @RequestBody UpdateCartItemRequest updateCartItemRequest){


        cartService.updateCart(updateCartItemRequest);
        return "Product Updated Successfully";
    }
    @DeleteMapping
    public String clearCart(){

        cartService.clearCart();

        return  "Cart Cleared successfully";
    }





}
