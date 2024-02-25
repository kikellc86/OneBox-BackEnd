package com.example.ecommerce.ecommercebackend.controller;

import com.example.ecommerce.ecommercebackend.model.Cart;
import com.example.ecommerce.ecommercebackend.model.Product;
import com.example.ecommerce.ecommercebackend.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @MockBean
    private CartService cartService;

    @Autowired
    private CartController cartController;

    @Test
    void testCreateCartSuccess() {

        Cart expectedCart = new Cart();
        when(cartService.createCart()).thenReturn(expectedCart);

        ResponseEntity<Cart> response = cartController.createCart();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(response.getBody()).isEqualTo(expectedCart);
        verify(cartService, times(1)).createCart();
        verify(cartService, times(1)).incrementMetric("createCart.calls.total");
    }

    @Test
    void testGetCartExistingCartSuccess() {

        Long cartId = 1L;
        Cart expectedCart = new Cart();
        when(cartService.getCart(cartId)).thenReturn(expectedCart);

        ResponseEntity<Cart> response = cartController.getCart(cartId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedCart);
        verify(cartService, times(1)).getCart(cartId);
        verify(cartService, times(1)).incrementMetric("getCart.calls.total");
        verify(cartService, times(1)).incrementMetric("getCart.calls.success");
        verify(cartService, never()).incrementMetric("getCart.calls.failure");
    }

    @Test
    void testGetCartNonExistingCartNotFound() {

        Long cartId = 2L;
        when(cartService.getCart(cartId)).thenReturn(null);

        ResponseEntity<Cart> response = cartController.getCart(cartId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(cartService, times(1)).getCart(cartId);
        verify(cartService, times(1)).incrementMetric("getCart.calls.total");
        verify(cartService, never()).incrementMetric("getCart.calls.success");
        verify(cartService, times(1)).incrementMetric("getCart.calls.failure");
    }

    @Test
    void testAddProductToCartSuccess() {

        Long cartId = 1L;
        Product product = new Product(1L, "Test Product", 10);
        Cart expectedCart = new Cart();
        when(cartService.addProductToCart(eq(cartId), any(Product.class))).thenReturn(expectedCart);

        ResponseEntity<Cart> response = cartController.addProductToCart(cartId, product);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedCart);
        verify(cartService, times(1)).addProductToCart(cartId, product);
        verify(cartService, times(1)).incrementMetric("addProductToCart.calls.total");
        verify(cartService, times(1)).incrementMetric("addProductToCart.calls.success");
        verify(cartService, never()).incrementMetric("addProductToCart.calls.failure");
    }

    @Test
    void testAddProductToCartNonExistingCartNotFound() {

        Long cartId = 2L;
        Product product = new Product(2L, "Another Test Product", 5);
        when(cartService.addProductToCart(eq(cartId), any(Product.class))).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addProductToCart(cartId, product);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(cartService, times(1)).addProductToCart(cartId, product);
        verify(cartService, times(1)).incrementMetric("addProductToCart.calls.total");
        verify(cartService, never()).incrementMetric("addProductToCart.calls.success");
        verify(cartService, times(1)).incrementMetric("addProductToCart.calls.failure");
    }

    @Test
    void testDeleteCartExistingCartSuccess() {

        Long cartId = 1L;
        when(cartService.deleteCart(cartId)).thenReturn(true);

        ResponseEntity<Void> response = cartController.deleteCart(cartId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(cartService, times(1)).deleteCart(cartId);
        verify(cartService, times(1)).incrementMetric("deleteCart.calls.total");
        verify(cartService, times(1)).incrementMetric("deleteCart.calls.success");
        verify(cartService, never()).incrementMetric("deleteCart.calls.failure");
    }

    @Test
    void testDeleteCartNonExistingCartNotFound() {

        Long cartId = 2L;
        when(cartService.deleteCart(cartId)).thenReturn(false);

        ResponseEntity<Void> response = cartController.deleteCart(cartId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(cartService, times(1)).deleteCart(cartId);
        verify(cartService, times(1)).incrementMetric("deleteCart.calls.total");
        verify(cartService, never()).incrementMetric("deleteCart.calls.success");
        verify(cartService, times(1)).incrementMetric("deleteCart.calls.failure");
    }

}
