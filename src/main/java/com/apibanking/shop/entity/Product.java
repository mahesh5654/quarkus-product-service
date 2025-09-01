package com.apibanking.shop.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Schema(name = "Product", description = "Represents a product in the inventory.")
public class Product extends PanacheEntity {

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "The name of the product.", example = "Laptop Pro Max")
    public String name;

    @NotBlank(message = "Description cannot be blank")
    @Schema(description = "A brief description of the product.", example = "High-performance laptop with advanced features.")
    public String description;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price must be non-negative")
    @Schema(description = "The price of the product.", example = "1350.00")
    public Double price;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be non-negative")
    @Schema(description = "The available quantity in stock.", example = "45")
    public Integer quantity;
}
