package com.ecommerce.orderservice.service;


import com.ecommerce.orderservice.client.CartClient;
import com.ecommerce.orderservice.client.ProductClient;
import com.ecommerce.orderservice.dto.*;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderItem;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.exception.CartNotFoundException;
import com.ecommerce.orderservice.exception.InsufficientStockException;
import com.ecommerce.orderservice.exception.ProductNotFoundException;
import com.ecommerce.orderservice.exception.ProductServiceUnavailableException;
import com.ecommerce.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final CartClient cartClient;
    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(CreateOrderRequest request){

        Order order = new Order();

        ProductResponse product =
                productClient.getProduct(request.productId());
        if(product==null) {
            throw  new ProductNotFoundException("Product not found");
        }
        if(product.quantity() < request.quantity()){
            throw new InsufficientStockException("Insufficient stock");
        }
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        OrderItem item = new OrderItem();

        item.setProductId(request.productId());
        item.setQuantity(request.quantity());

        item.setPriceAtPurchase(product.price());

        item.setOrder(order);

        order.getItems().add(item);

        order.setTotalAmount(
                item.getPriceAtPurchase() * item.getQuantity()
        );

        Order orderOutput= orderRepository.save(order);

        productClient.reduceStock(request.productId(), request.quantity());
        return orderOutput;
    }


    @Transactional
    @CircuitBreaker(name = "checkoutService",fallbackMethod = "checkoutFallback")
    public String checkOut(CheckoutRequest checkoutRequest) {
        CartResponse cartResponse;
        try {
             cartResponse =cartClient.getCart(checkoutRequest.userEmail());
        }catch (HttpClientErrorException.NotFound ex){
            throw new CartNotFoundException("Cart not Found");
        }
        if(cartResponse==null || cartResponse.cartItemResposneList().isEmpty()){
            throw new CartNotFoundException("Cart is Empty");
        }

        Order order= new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        for(CartItemResponse cartItemResponse:cartResponse.cartItemResposneList()){
            ProductResponse productResponse;

            try {
                productResponse = productClient.getProduct(cartItemResponse.productId());

            } catch (HttpClientErrorException.NotFound ex) {
                throw new ProductNotFoundException("Product Not Found");
            }
           productClient.reduceStock(cartItemResponse.productId(),cartItemResponse.quantity());


            OrderItem orderItem=new OrderItem();
            orderItem.setQuantity(cartItemResponse.quantity());
            orderItem.setPriceAtPurchase(cartItemResponse.priceAtPurchase());
            orderItem.setProductId(cartItemResponse.productId());
            orderItem.setOrder(order);
            order.getItems().add(orderItem);


        }

        double totalAmount= order.getItems().stream()
                .mapToDouble(i->i.getPriceAtPurchase()*i.getQuantity()).sum();
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);
       cartClient.deleteCart(checkoutRequest.userEmail());
        return "Order Created Successfully";

    }

    public String checkoutFallback(CheckoutRequest request, Throwable ex) {
        System.out.println("Circuit Breaker activated: " + ex.getMessage());
        throw new ProductServiceUnavailableException("Check out Service is temporarily unavailable");
    }
}
