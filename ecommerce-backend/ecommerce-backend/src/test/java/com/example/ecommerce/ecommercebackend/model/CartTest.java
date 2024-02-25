package com.example.ecommerce.ecommercebackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Test
    void testNoArgsConstructor() {
        assertNull(cart.getId());
        assertEquals(0, cart.getProducts().size());
        assertNotNull(cart.getCreationTime());
    }

    @Test
    void testIdProperty() {
        Long expectedId = 1L;
        cart.setId(expectedId);
        assertEquals(expectedId, cart.getId());
    }

    @Test
    void testProductsProperty() {
        Product product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");
        product.setAmount(10);

        cart.addProduct(product);

        assertNotNull(cart.getProducts());
        assertFalse(cart.getProducts().isEmpty());
        assertEquals(1, cart.getProducts().size());
        assertEquals(product, cart.getProducts().get(0));
    }

    @Test
    void testCreationTimeProperty() {
        assertNotNull(cart.getCreationTime());
        assertTrue(cart.getCreationTime().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testAddProduct() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setDescription("Test Product 1");
        product1.setAmount(5);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setDescription("Test Product 2");
        product2.setAmount(10);

        cart.addProduct(product1);
        cart.addProduct(product2);

        List<Product> products = cart.getProducts();
        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));
    }
}
