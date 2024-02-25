package com.example.ecommerce.ecommercebackend.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cart {
    private Long id;
    private List<Product> products = new ArrayList<>();
    private LocalDateTime creationTime;

    // Constructor
    public Cart() {
        this.creationTime = LocalDateTime.now();
    }

    // Method to add a product to the cart
    public void addProduct(Product product) {
        this.products.add(product);
    }
}
