package com.example.ecommerce.ecommercebackend.service;

import com.example.ecommerce.ecommercebackend.model.Cart;
import com.example.ecommerce.ecommercebackend.model.Product;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    public void setup() {

        MeterRegistry meterRegistry = new SimpleMeterRegistry();
        cartService = new CartService(meterRegistry);
    }

    @Test
    void testCreateCart() {

        Cart cart = cartService.createCart();
        assertNotNull(cart);
        assertNotNull(cart.getId());
    }

    @Test
    void testGetCartSuccess() {

        Cart createdCart = cartService.createCart();
        Cart retrievedCart = cartService.getCart(createdCart.getId());
        assertEquals(createdCart, retrievedCart);
    }

    @Test
    void testGetCartNotFound() {

        cartService.createCart();
        Cart retrievedCart = cartService.getCart(2L);
        assertNotNull(cartService.getCart(1L));
        assertNull(retrievedCart);
    }

    @Test
    void testAddProductToCart() {

        Cart cart = cartService.createCart();
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");
        product.setAmount(1);

        Cart updatedCart = cartService.addProductToCart(cart.getId(), product);

        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getProducts().size());
        assertEquals(product, updatedCart.getProducts().get(0));
    }

    @Test
    void testAddProductToCartWithInvalidProduct() {

        Cart cart = cartService.createCart();
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");
        product.setAmount(-1);

        Cart updatedCart = cartService.addProductToCart(cart.getId(), product);

        assertNull(updatedCart);
    }

    @Test
    void testAddProductToCartWithExistingProduct() {

        Cart cart = cartService.createCart();
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");
        product.setAmount(1);

        cartService.addProductToCart(cart.getId(), product);
        Cart updatedCart = cartService.addProductToCart(cart.getId(), product);

        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getProducts().size());
        assertEquals(2, updatedCart.getProducts().get(0).getAmount());
    }

    @Test
    void testAdd2DifferentProductsToCart() {

        Cart cart = cartService.createCart();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setDescription("Test Product 1");
        product1.setAmount(1);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setDescription("Test Product 2");
        product2.setAmount(1);

        cartService.addProductToCart(cart.getId(), product1);
        Cart updatedCart = cartService.addProductToCart(cart.getId(), product2);

        assertNotNull(updatedCart);
        assertEquals(2, updatedCart.getProducts().size());
        assertTrue(updatedCart.getProducts().contains(product1));
        assertTrue(updatedCart.getProducts().contains(product2));
    }

    @Test
    void testDeleteCartSuccess() {

        Cart cart = cartService.createCart();
        assertNotNull(cartService.getCart(cart.getId()));
        assertTrue(cartService.deleteCart(cart.getId()));
        assertNull(cartService.getCart(cart.getId()));
    }

    @Test
    void testDeleteCartNotFound() {

        Cart cart = cartService.createCart();
        assertNotNull(cartService.getCart(cart.getId()));

        assertFalse(cartService.deleteCart(2L));
    }

    @Test
    void testValidateProductWithValidProduct() {

        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");
        product.setAmount(1);
        assertTrue(cartService.validateProduct(product));
    }

    @Test
    void testValidateProductWithInvalidProduct() {

        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");
        product.setAmount(-1);
        assertFalse(cartService.validateProduct(product));
    }

    @Test
    void testScheduledRemoveOfExpiredCartsRemovesExpiredCartsOnly() {

        LocalDateTime now = LocalDateTime.now();
        Cart expiredCart = new Cart();
        expiredCart.setId(1L);
        expiredCart.setCreationTime(now.minusMinutes(10));
        Cart activeCart = new Cart();
        activeCart.setId(2L);
        activeCart.setCreationTime(now.minusMinutes(9));

        cartService.carts.put(expiredCart.getId(), expiredCart);
        cartService.carts.put(activeCart.getId(), activeCart);

        cartService.removeExpiredCarts();

        assertNull(cartService.getCart(expiredCart.getId()));
        assertNotNull(cartService.getCart(activeCart.getId()));
        assertEquals(1, cartService.meterRegistry.get("carts.deleted").counter().count());
        assertEquals(1, cartService.meterRegistry.get("carts.expired").counter().count());
    }

}
