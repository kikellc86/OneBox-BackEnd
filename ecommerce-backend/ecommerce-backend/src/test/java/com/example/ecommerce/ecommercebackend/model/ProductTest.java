package com.example.ecommerce.ecommercebackend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testNoArgsConstructor() {
        Product product = new Product();
        assertNull(product.getId());
        assertNull(product.getDescription());
        assertNull(product.getAmount());
    }

    @Test
    void testAllArgsConstructor() {
        Long expectedId = 1L;
        String expectedDescription = "Test Product";
        Integer expectedAmount = 10;

        Product product = new Product(expectedId, expectedDescription, expectedAmount);

        assertEquals(expectedId, product.getId());
        assertEquals(expectedDescription, product.getDescription());
        assertEquals(expectedAmount, product.getAmount());
    }

    @Test
    void testSettersAndGetters() {
        Product product = new Product();

        Long expectedId = 2L;
        String expectedDescription = "Another Test Product";
        Integer expectedAmount = 20;

        product.setId(expectedId);
        product.setDescription(expectedDescription);
        product.setAmount(expectedAmount);

        assertEquals(expectedId, product.getId());
        assertEquals(expectedDescription, product.getDescription());
        assertEquals(expectedAmount, product.getAmount());
    }
}
