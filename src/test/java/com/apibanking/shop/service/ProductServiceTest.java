package com.apibanking.shop.service;

import com.apibanking.shop.entity.Product;
import com.apibanking.shop.exception.ProductNotFoundException;
import com.apibanking.shop.exception.ValidationException;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

class ProductServiceTest {

    private ProductService productService;

    private MockedStatic<Product> productMock;   // Mock static PanacheEntityBase methods
    private MockedStatic<Panache> panacheMock;    // Mock Panache.withTransaction

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        productMock = mockStatic(Product.class);
        panacheMock = mockStatic(Panache.class);
    }

    @AfterEach
    void tearDown() {
        productMock.close();
        panacheMock.close();
    }

    @Test
    void testCreateProductValid() {
        Product newProduct = new Product();
        newProduct.name = "New";
        newProduct.price = 100.0;
        newProduct.quantity = 10;

        panacheMock.when(() -> Panache.withTransaction(any()))
                .thenReturn(Uni.createFrom().item(newProduct));

        Product result = productService.create(newProduct).await().indefinitely();
        assertEquals("New", result.name);
    }

    @Test
    void testCreateProductInvalidPrice() {
        Product invalid = new Product();
        invalid.price = -5.0;
        invalid.quantity = 1;

        assertThrows(ValidationException.class,
                () -> productService.create(invalid));
    }

    @Test
    void testDeleteProduct() {
        panacheMock.when(() -> Panache.withTransaction(any()))
                .thenReturn(Uni.createFrom().item(true));

        Boolean result = productService.delete(1L).await().indefinitely();
        assertTrue(result);
    }


}
