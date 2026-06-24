package com.ecommerce.cartservice.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private double totalAmount;
    @JsonManagedReference
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CartItem> cartItemList= new ArrayList<>();

}
