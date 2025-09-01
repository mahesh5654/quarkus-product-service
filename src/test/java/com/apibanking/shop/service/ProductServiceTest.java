package com.apibanking.shop.service;

import com.apibanking.shop.entity.Product;
import com.apibanking.shop.exception.ProductNotFoundException;
import com.apibanking.shop.exception.ValidationException;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    ProductService productService;

    @BeforeEach
    public void setup() {
        productService = new ProductService();
    }

    @Test
    public void testCreateProduct_success() {
        Product product = new Product();
        product.price = 10.0;
        product.quantity = 5;

        try (MockedStatic<Panache> panache = mockStatic(Panache.class)) {
            panache.when(() -> Panache.withTransaction(any()))
                    .thenReturn(Uni.createFrom().item(product));

            Product result = productService.create(product).await().indefinitely();
            assertEquals(10.0, result.price);
        }
    }

    @Test
    public void testCreateProduct_validationFailure() {
        Product product = new Product();
        product.price = -1d;
        product.quantity = 1;

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            productService.create(product).await().indefinitely();
        });

        assertEquals("Product price and quantity must be non-negative.", ex.getMessage());
    }

    @Test
    public void testGetAllProducts() {
        ProductService spyService = spy(new ProductService());
        List<Product> products = List.of(new Product(), new Product());

        doReturn(Uni.createFrom().item(products)).when(spyService).getAll();

        List<Product> result = spyService.getAll().await().indefinitely();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetById_productExists() {
        Product product = new Product();
        product.id = 1L;

        // Spy on the service so we can override findProductById
        ProductService spyService = spy(new ProductService());

        // Stub the protected method
        doReturn(Uni.createFrom().item(product))
                .when(spyService)
                .findProductById(1L);

        // Call the actual getById
        Product result = spyService.getById(1L).await().indefinitely();

        assertEquals(1L, result.id);
    }

    @Test
    public void testGetById_productNotFound() {
        ProductService spyService = spy(new ProductService());

        // Return null item to simulate not found
        doReturn(Uni.createFrom().nullItem())
                .when(spyService)
                .findProductById(1L);

        assertThrows(ProductNotFoundException.class, () -> {
            spyService.getById(1L).await().indefinitely();
        });
    }

    @Test
    public void testUpdate_validationFailure() {
        Product input = new Product();
        input.price = -100d;
        input.quantity = 1;

        assertThrows(ValidationException.class, () -> {
            productService.update(1L, input).await().indefinitely();
        });
    }

    @Test
    public void testDelete_success() {
        try (MockedStatic<Panache> panache = mockStatic(Panache.class)) {
            panache.when(() -> Panache.withTransaction(any()))
                    .thenReturn(Uni.createFrom().item(true));

            Boolean result = productService.delete(1L).await().indefinitely();
            assertTrue(result);
        }
    }

    @Test
    public void testDelete_notFound() {
        try (MockedStatic<Panache> panache = mockStatic(Panache.class)) {
            panache.when(() -> Panache.withTransaction(any()))
                    .thenReturn(Uni.createFrom().nullItem());

            assertThrows(ProductNotFoundException.class, () -> {
                productService.delete(1L).await().indefinitely();
            });
        }
    }

    @Test
    public void testCheckStock_enoughStock() {
        Product product = new Product();
        product.quantity = 10;

        ProductService spyService = spy(productService);
        doReturn(Uni.createFrom().item(product))
                .when(spyService).findProductById(1L);

        Boolean inStock = spyService.checkStock(1L, 5).await().indefinitely();
        assertTrue(inStock);
    }

    @Test
    public void testCheckStock_notFound() {
        ProductService spyService = spy(productService);
        doReturn(Uni.createFrom().nullItem())
                .when(spyService).findProductById(1L);

        assertThrows(ProductNotFoundException.class, () -> {
            spyService.checkStock(1L, 5).await().indefinitely();
        });
    }
    @Test
    public void testSortByPrice() {
        List<Product> sorted = List.of(new Product(), new Product());

        ProductService spyService = spy(productService);
        doReturn(Uni.createFrom().item(sorted))
                .when(spyService).listProductsSortedByPrice();

        List<Product> result = spyService.sortByPrice().await().indefinitely();
        assertEquals(2, result.size());
    }
}
