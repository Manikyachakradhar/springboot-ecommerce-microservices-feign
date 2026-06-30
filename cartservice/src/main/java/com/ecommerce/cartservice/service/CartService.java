package com.ecommerce.cartservice.service;

import com.ecommerce.cartservice.client.ProductClient;
import com.ecommerce.cartservice.dto.*;
import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.entity.CartItem;
import com.ecommerce.cartservice.exception.CartNotFoundException;
import com.ecommerce.cartservice.exception.InsufficientStockException;
import com.ecommerce.cartservice.exception.ProductNotFound;
import com.ecommerce.cartservice.exception.ProductServiceUnavailableException;
import com.ecommerce.cartservice.repository.CartRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductClient productClient;

    private final CartRepository cartRepository;

    @Transactional
    @CircuitBreaker(name = "productService",fallbackMethod = "addToCartFallback")
    public String  addToCart( AddToCartRequest addToCartRequest) {
        Cart cart = cartRepository.findByUserEmail(addToCartRequest.userEmail())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserEmail(addToCartRequest.userEmail());
                    return newCart;
                });
        ProductResponse productResponse;
        try {

             productResponse = productClient.getProduct(addToCartRequest.productId());
        }
        catch (HttpClientErrorException.NotFound exception){
            throw new ProductNotFound("Product Not found");

        }
        if(productResponse.quantity() < addToCartRequest.quantity()){
            throw new InsufficientStockException("Insufficient stock");
        }

        CartItem existigCartItem= cart.getCartItemList().stream().filter(item->item.getProductId()
                .equals(addToCartRequest.productId())).findFirst().orElse(null);
        if(existigCartItem!=null){
            existigCartItem.setQuantity(
                    existigCartItem.getQuantity()
                            + addToCartRequest.quantity());
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(productResponse.id());
            cartItem.setProductName(productResponse.name());
            cartItem.setQuantity(addToCartRequest.quantity());
            cartItem.setPriceAtPurchase(productResponse.price());
            cartItem.setCart(cart);
            cart.getCartItemList().add(cartItem);
        }

        double totalAmount= cart.getCartItemList().stream().mapToDouble(
                i->i.getPriceAtPurchase()*i.getQuantity()).sum();
        cart.setTotalAmount(totalAmount);

        cartRepository.save(cart);

        return  "Cart Added Successfully";
    }

    public CartResponse getCart(String userEmail) {
        Cart cart=cartRepository.findByUserEmail(userEmail)
                .orElseThrow(()->new CartNotFoundException("Cart Not found"));
        return new CartResponse(
                cart.getId(),
                cart.getUserEmail(),
                cart.getTotalAmount(),
                cart.getCartItemList().stream()
                        .map(i -> new CartItemResponse(
                                i.getProductId(),
                                i.getProductName(),
                                i.getQuantity(),
                                i.getPriceAtPurchase()
                        ))
                        .toList()
        );
    }

    @Transactional
    public void deleteProductFromCarr(String userEmail, Long productId) {

       Cart cart= cartRepository.findByUserEmail(userEmail).
               orElseThrow(()->new CartNotFoundException("Cart Not Found")
       );

       CartItem cartItem=cart.getCartItemList().stream()
               .filter(i->i.getProductId().equals(productId)).findFirst()
               .orElseThrow(()->new ProductNotFound("Product Not found"));

       cart.getCartItemList().remove(cartItem);
        double totalAmount= cart.getCartItemList()
                .stream()
                .mapToDouble(i->i.getQuantity()*i.getPriceAtPurchase()).sum();
        cart.setTotalAmount(totalAmount);
       cartRepository.save(cart);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "updateCartFallback")
    public void updateCart( UpdateCartItemRequest updateCartItemRequest) {
        Cart cart=cartRepository.findByUserEmail(updateCartItemRequest.userEmail())
                .orElseThrow(()->new CartNotFoundException("Cart Not Found"));

        CartItem cartItem=cart.getCartItemList().stream()
                .filter(i->i.getProductId().equals(updateCartItemRequest.productId())).findFirst()
                .orElseThrow(()->new ProductNotFound("Product Not Found"));

        ProductResponse productResponse;
        try {
             productResponse = productClient.getProduct(updateCartItemRequest.productId());
        }
        catch (HttpClientErrorException ex){
            throw new ProductNotFound("Product Not Found");
        }
        if(productResponse.quantity()<updateCartItemRequest.quantity()){
            throw new InsufficientStockException("Insufficient Stock");
        }
        cartItem.setQuantity(updateCartItemRequest.quantity());

        double totalAmount=cart.getCartItemList()
                .stream().mapToDouble(i->i.getPriceAtPurchase()*i.getQuantity()).sum();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);



    }

    @Transactional
    public void clearCart(String userEmail) {

        Cart cart=cartRepository.findByUserEmail(userEmail).orElseThrow(()->
                new CartNotFoundException("Cart Not Found"));

        cart.getCartItemList().clear();
        cart.setTotalAmount(0);
        cartRepository.save(cart);
    }

    public String  addToCartFallback( AddToCartRequest addToCartRequest,Throwable ex){

        return "Product Service is Temporarily Unavailable";
    }

    public void updateCartFallback(UpdateCartItemRequest request, Throwable ex) {
        throw new ProductServiceUnavailableException("Product Service is temporarily unavailable.");
    }
}
