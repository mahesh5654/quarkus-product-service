package com.apibanking.shop.service;

import com.apibanking.shop.entity.Product;
import com.apibanking.shop.exception.ProductNotFoundException;
import com.apibanking.shop.exception.ValidationException;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductService {

    public Uni<Product> create(Product product) {
        // Add a simple check for invalid data
        if (product.price < 0 || product.quantity < 0) {
            throw new ValidationException("Product price and quantity must be non-negative.");
        }
        return Panache.withTransaction(product::persist);
    }

    public Uni<List<Product>> getAll() {
        return Product.listAll();
    }

    public Uni<Product> getById(Long id) {
        return findProductById(id)
                .onItem()
                .ifNull()
                .failWith(() -> new ProductNotFoundException("Product with ID " + id + " not found."));
    }

    // Make this protected to allow mocking
    protected Uni<Product> findProductById(Long id) {
        return Product.findById(id);
    }

    public Uni<Product> update(Long id, Product product) {
        if (product.price < 0 || product.quantity < 0) {
            throw new ValidationException("Product price and quantity must be non-negative.");
        }
        return Panache.withTransaction(() -> Product.<Product>findById(id)
                .onItem().ifNotNull().invoke(entity -> {
                    entity.name = product.name;
                    entity.description = product.description;
                    entity.price = product.price;
                    entity.quantity = product.quantity;
                }));
    }

    public Uni<Boolean> delete(Long id) {
        return Panache.withTransaction(() -> Product.deleteById(id))
                .map(deleted -> deleted)
                .onItem().ifNull().failWith(() -> new ProductNotFoundException("Product with ID " + id + " not found."));
    }

    public Uni<Boolean> checkStock(Long id, int count) {
        return findProductById(id)
                .onItem().ifNotNull().transform(product -> product.quantity >= count)
                .onItem().ifNull().failWith(() -> new ProductNotFoundException("Product with ID " + id + " not found."));
    }

    protected Uni<List<Product>> listProductsSortedByPrice() {
        return Product.list("ORDER BY price ASC");
    }

    public Uni<List<Product>> sortByPrice() {
        return listProductsSortedByPrice();
    }
}
