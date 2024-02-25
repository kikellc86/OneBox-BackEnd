package com.example.ecommerce.ecommercebackend.controller;

import com.example.ecommerce.ecommercebackend.model.Cart;
import com.example.ecommerce.ecommercebackend.model.Product;
import com.example.ecommerce.ecommercebackend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart() {

        // Increment the total createCart calls counter
        cartService.incrementMetric("createCart.calls.total");

        return ResponseEntity.ok(cartService.createCart());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {

        // Increment the total getCart calls counter
        cartService.incrementMetric("getCart.calls.total");

        Cart cart = cartService.getCart(cartId);

        if (cart != null) {

            // Increment the successful getCart calls counter
            cartService.incrementMetric("getCart.calls.success");

            return ResponseEntity.ok(cart);
        } else {

            // Increment the unsuccessful getCart calls counter
            cartService.incrementMetric("getCart.calls.failure");

            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{cartId}/products")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long cartId, @RequestBody Product product) {

        // Increment the total addProductToCart calls counter
        cartService.incrementMetric("addProductToCart.calls.total");

        Cart cart = cartService.addProductToCart(cartId, product);

        if (cart != null) {

            // Increment the successful addProductToCart calls counter
            cartService.incrementMetric("addProductToCart.calls.success");

            return ResponseEntity.ok(cart);
        } else {

            // Increment the unsuccessful addProductToCart calls counter
            cartService.incrementMetric("addProductToCart.calls.failure");

            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {

        // Increment the total deleteCart calls counter
        cartService.incrementMetric("deleteCart.calls.total");

        if (cartService.deleteCart(id)) {

            // Increment the successful deleteCart calls counter
            cartService.incrementMetric("deleteCart.calls.success");
            // Increment the total carts.deleted counter
            cartService.incrementMetric("carts.deleted");

            return ResponseEntity.ok().build();
        } else {

            // Increment the unsuccessful deleteCart calls counter
            cartService.incrementMetric("deleteCart.calls.failure");

            return ResponseEntity.notFound().build();
        }
    }
}
