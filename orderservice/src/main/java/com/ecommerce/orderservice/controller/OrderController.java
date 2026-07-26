    package com.ecommerce.orderservice.controller;

    import com.ecommerce.orderservice.dto.CheckoutRequest;
    import com.ecommerce.orderservice.dto.CreateOrderRequest;
    import com.ecommerce.orderservice.dto.OrderHistoryResponse;
    import com.ecommerce.orderservice.entity.Order;
    import com.ecommerce.orderservice.service.OrderService;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/order")
    @RequiredArgsConstructor
    public class OrderController {

        private final OrderService orderService;

        @PostMapping
        public Order createOrder(
                @Valid @RequestBody CreateOrderRequest request){

            return orderService.createOrder(request);
        }
        @PostMapping("/checkOut")
        public  String checkOut(){
            return orderService.checkOut();
        }
        @GetMapping("/history")
        public List<OrderHistoryResponse> getMyOrders() {
            return orderService.getMyOrders();
        }

        @GetMapping("/{orderId}")
        public OrderHistoryResponse getOrderById(@PathVariable Long orderId) {
            return orderService.getOrderById(orderId);
        }
        @PutMapping("/{orderId}/cancel")
        public String cancelOrder(@PathVariable Long orderId) {
            return orderService.cancelOrder(orderId);
        }


    }
