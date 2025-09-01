package com.apibanking.shop.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidProduct() {
        Product product = new Product("Laptop Pro Max", "High-performance laptop", 1350.00, 45);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    public void testBlankNameFailsValidation() {
        Product product = new Product("", "Some description", 100.0, 5);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && v.getMessage().contains("cannot be blank")));
    }

    @Test
    public void testNegativePriceFailsValidation() {
        Product product = new Product("Product A", "Desc", -5.0, 10);

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price") && v.getMessage().contains("non-negative")));
    }

    // Add other validation tests similarly for description, quantity, null checks etc.
}
