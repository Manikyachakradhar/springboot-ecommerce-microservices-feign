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
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import io.swagger.v3.oas.annotations.responses.ApiResponses;
    import io.swagger.v3.oas.annotations.tags.Tag;

    import java.util.List;

    @RestController
    @RequestMapping("/api/order")
    @RequiredArgsConstructor
    @Tag(name = "Order APIs", description = "Order Management APIs")
    public class OrderController {

        private final OrderService orderService;

        @Operation(
                summary = "Create Order",
                description = "Creates a new order for a single product."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Order created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid request or insufficient stock"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Product not found")
        })
        @PostMapping
        public Order createOrder(
                @Valid @RequestBody CreateOrderRequest request){

            return orderService.createOrder(request);
        }
        @Operation(
                summary = "Checkout Cart",
                description = "Creates an order using all products available in the authenticated user's cart."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Checkout completed successfully"),
                @ApiResponse(responseCode = "400", description = "Cart is empty"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Cart not found"),
                @ApiResponse(responseCode = "503", description = "Product Service unavailable")
        })
        @PostMapping("/checkOut")
        public  String checkOut(){
            return orderService.checkOut();
        }
        @Operation(
                summary = "Get Order History",
                description = "Returns all orders placed by the authenticated user."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
                @ApiResponse(responseCode = "401", description = "Unauthorized")
        })
        @GetMapping("/history")
        public List<OrderHistoryResponse> getMyOrders() {
            return orderService.getMyOrders();
        }

        @Operation(
                summary = "Get Order By ID",
                description = "Returns details of a specific order belonging to the authenticated user."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Access denied"),
                @ApiResponse(responseCode = "404", description = "Order not found")
        })
        @GetMapping("/{orderId}")
        public OrderHistoryResponse getOrderById(@PathVariable Long orderId) {
            return orderService.getOrderById(orderId);
        }
        @Operation(
                summary = "Cancel Order",
                description = "Cancels an order if it is still in CREATED status and restores the product stock."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
                @ApiResponse(responseCode = "400", description = "Order cannot be cancelled"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "403", description = "Access denied"),
                @ApiResponse(responseCode = "404", description = "Order not found")
        })
        @PutMapping("/{orderId}/cancel")
        public String cancelOrder(@PathVariable Long orderId) {
            return orderService.cancelOrder(orderId);
        }


    }
