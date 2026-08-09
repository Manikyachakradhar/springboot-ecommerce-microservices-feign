    package com.ecommerce.orderservice.service;


    import com.ecommerce.orderservice.client.CartClient;
    import com.ecommerce.orderservice.client.ProductClient;
    import com.ecommerce.orderservice.dto.*;
    import com.ecommerce.orderservice.entity.Order;
    import com.ecommerce.orderservice.entity.OrderItem;
    import com.ecommerce.orderservice.entity.OrderStatus;
    import com.ecommerce.orderservice.exception.*;
    import com.ecommerce.orderservice.repository.OrderRepository;
    import feign.FeignException;
    import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Service;
    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class OrderService {

        private final ProductClient productClient;
        private final CartClient cartClient;
        private final OrderRepository orderRepository;

        @Transactional
        public Order createOrder(CreateOrderRequest request){

            String userEmail = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            Order order = new Order();

            ProductResponse product =
                    productClient.getProduct(request.productId());
            if(product==null) {
                throw  new ProductNotFoundException("Product not found");
            }
            if(product.quantity() < request.quantity()){
                throw new InsufficientStockException("Insufficient stock");
            }
            order.setUserEmail(userEmail);
            order.setCreatedAt(LocalDateTime.now());
            order.setStatus(OrderStatus.CREATED);

            OrderItem item = new OrderItem();

            item.setProductId(request.productId());
            item.setQuantity(request.quantity());
            item.setProductName(product.name());
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
        public String checkOut() {

            String userEmail = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            CartResponse cartResponse;
            try {
                 cartResponse =cartClient.getCart();
            }catch (FeignException.NotFound ex){
                throw new CartNotFoundException("Cart not Found");
            }
            if(cartResponse==null || cartResponse.cartItemResposneList().isEmpty()){
                throw new CartNotFoundException("Cart is Empty");
            }

            Order order= new Order();
            order.setCreatedAt(LocalDateTime.now());
            order.setStatus(OrderStatus.CREATED);
            order.setUserEmail(userEmail);

            for(CartItemResponse cartItemResponse:cartResponse.cartItemResposneList()){
                ProductResponse productResponse;

                try {
                    productResponse = productClient.getProduct(cartItemResponse.productId());

                } catch (FeignException.NotFound ex) {
                    throw new ProductNotFoundException("Product Not Found");
                }
               productClient.reduceStock(cartItemResponse.productId(),cartItemResponse.quantity());


                OrderItem orderItem=new OrderItem();
                orderItem.setQuantity(cartItemResponse.quantity());
                orderItem.setPriceAtPurchase(cartItemResponse.priceAtPurchase());
                orderItem.setProductId(cartItemResponse.productId());
                orderItem.setProductName(productResponse.name());
                orderItem.setOrder(order);
                order.getItems().add(orderItem);


            }

            double totalAmount= order.getItems().stream()
                    .mapToDouble(i->i.getPriceAtPurchase()*i.getQuantity()).sum();
            order.setTotalAmount(totalAmount);

            orderRepository.save(order);
           cartClient.deleteCart();
            return "Order Created Successfully";

        }

        public String checkoutFallback(Throwable ex) {
            if (ex instanceof CartNotFoundException) {
                throw (CartNotFoundException) ex;
            }

            if (ex instanceof ProductNotFoundException) {
                throw (ProductNotFoundException) ex;
            }

            if (ex instanceof InsufficientStockException) {
                throw (InsufficientStockException) ex;
            }
            throw new ProductServiceUnavailableException("Check out Service is temporarily unavailable");
        }

        public List<OrderHistoryResponse> getMyOrders(){

            String userEmail = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();

            List<Order> orders = orderRepository.findByUserEmail(userEmail);

           return orders.stream().map(this::mapToOrderHistoryResponse).toList();
        }

        public OrderHistoryResponse getOrderById(Long orderId){
            String userEmail = SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() ->
                            new OrderNotFoundException("Order not found"));

            if (!order.getUserEmail().equals(userEmail)) {
                throw new AccessDeniedException("You are not allowed to view this order.");
            }

            return mapToOrderHistoryResponse(order);

        }


    private OrderHistoryResponse mapToOrderHistoryResponse(Order order) {
        return new OrderHistoryResponse(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getProductName(),
                                item.getQuantity(),
                                item.getPriceAtPurchase()
                        ))
                        .toList()
        );
    }
            @Transactional
            public String cancelOrder(Long orderId) {

                String userEmail = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new OrderNotFoundException("Order not found"));

                if (!order.getUserEmail().equals(userEmail)) {
                    throw new AccessDeniedException(
                            "You are not allowed to cancel this order."
                    );
                }
                if (order.getStatus() != OrderStatus.CREATED) {
                    throw new OrderCancellationException(
                            "Only CREATED orders can be cancelled."
                    );
                }
                for (OrderItem item : order.getItems()) {

                    productClient.increaseStock(
                            item.getProductId(),
                            item.getQuantity()
                    );
                }
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                return "Order cancelled successfully";
            }

    }
