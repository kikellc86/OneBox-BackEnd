package com.example.ecommerce.ecommercebackend.service;

import com.example.ecommerce.ecommercebackend.model.Cart;
import com.example.ecommerce.ecommercebackend.model.Product;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartService {
    final ConcurrentHashMap<Long, Cart> carts = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();
    final MeterRegistry meterRegistry;
    Logger logger = LoggerFactory.getLogger(CartService.class);

    public CartService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    // Create a new cart
    public Cart createCart() {
        long id = counter.incrementAndGet();
        Cart cart = new Cart();
        cart.setId(id);
        carts.put(id, cart);
        logger.info("Creating cart with ID: {}", id);
        return cart;
    }

    // Get cart by ID
    public Cart getCart(Long id) {
        Cart cart = carts.get(id);
        if (cart != null) {
            logger.info("Retrieving cart with ID: {}", id);
        } else {
            logger.info("Cart with ID: {} not found", id);
        }
        return cart;
    }

    // Add a product to a cart
    public Cart addProductToCart(Long cartId, Product product) {
        Cart cart = carts.get(cartId);
        boolean validateProduct = validateProduct(product);
        if (cart != null && validateProduct) {
            // Check if the product is already in the cart
            Product productFromCart = getProductFromCart(cartId, product);
            // If the product is already in the cart, add the new amount to the existing amount
            if (productFromCart != null) {
                product.setAmount(product.getAmount() + productFromCart.getAmount());
                cart.getProducts().remove(productFromCart);
            }
            logger.info("Adding product: {} to cart with ID: {}", product, cartId);
            cart.addProduct(product);
            return carts.get(cartId);
        } else {
            if (cart == null) {
                logger.info("Cart with ID: {} not found", cartId);
            }
            if (!validateProduct) {
                logger.info("Invalid product: {}", product);
            }
            return null;
        }
    }

    // Validate the product
    public boolean validateProduct(Product product) {
        return product.getAmount() != null && product.getAmount() > 0 &&
                product.getId() > 0 && product.getDescription() != null &&
                !product.getDescription().isEmpty();
    }

    // Get a product from a cart
    public Product getProductFromCart(Long cartId, Product aProduct) {
        Cart cart = carts.get(cartId);
        if (cart != null) {
            return cart.getProducts().stream().filter(product -> product.getId().equals(aProduct.getId()) &&
                    product.getDescription().equals(aProduct.getDescription())).findFirst().orElse(null);
        }
        return null;
    }

    // Delete a cart
    public boolean deleteCart(Long id) {
        Cart cart = carts.get(id);
        if (cart != null) {
            carts.remove(id);
            logger.info("Removing cart with ID: {}", id);
            return true;
        } else {
            logger.info("Cart with ID: {} not found", id);
            return false;
        }
    }

    // Scheduled task that runs every 1 second to remove expired carts (older than 10 minutes)
    @Scheduled(fixedRate = 1000)
    public void removeExpiredCarts() {
        LocalDateTime now = LocalDateTime.now();

        carts.entrySet().removeIf(entry -> {

            boolean isExpired = ChronoUnit.MINUTES.between(entry.getValue().getCreationTime(), now) >= 10;

            if (isExpired) {
                // Log the deletion of an individual cart
                logger.info("Removing expired cart with ID: {}", entry.getKey());

                // Increment the expired carts counter
                incrementMetric("carts.expired");
                // Increment the total removed carts counter
                incrementMetric("carts.deleted");
            }
            return isExpired;
        });
    }

    public void incrementMetric(String name) {
        meterRegistry.counter(name).increment();
    }
}
