package com.apibanking.shop.repository;

import com.apibanking.shop.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    // You get all the standard Panache methods for free.
    // For example, productRepository.findById(id), productRepository.listAll(), etc.
    // You can also add custom queries here.

}