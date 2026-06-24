package com.ecommerce.orderservice.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "order",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
    private List<OrderItem> items= new ArrayList<>();
}
