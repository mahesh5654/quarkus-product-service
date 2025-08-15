package com.apibanking.shop.rest;

import com.apibanking.shop.entity.Product;
import com.apibanking.shop.service.ProductService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product Resource", description = "Operations related to products in the store.")
public class ProductResource {

    @Inject
    ProductService productService;

    @POST
    @Operation(summary = "Create a new product", description = "Adds a new product to the database.")
    @APIResponse(responseCode = "201", description = "Product created successfully.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Product.class)))
    @APIResponse(responseCode = "400", description = "Invalid product data provided.")
    public Uni<Response> create(Product product) {
        return productService.create(product)
                .map(saved -> Response.status(Response.Status.CREATED).entity(saved).build());
    }

    @GET
    @Operation(summary = "Get all products", description = "Retrieves a list of all products in the store.")
    @APIResponse(responseCode = "200", description = "List of all products.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Product.class)))
    public Uni<List<Product>> getAll() {
        return productService.getAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves a specific product using its ID.")
    @APIResponse(responseCode = "200", description = "The product with the given ID.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Product.class)))
    @APIResponse(responseCode = "404", description = "Product not found.")
    public Uni<Response> getById(@PathParam("id") Long id) {
        return productService.getById(id)
                .map(product -> product != null ? Response.ok(product) : Response.status(Response.Status.NOT_FOUND))
                .map(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a product", description = "Updates an existing product's details.")
    @APIResponse(responseCode = "200", description = "Product updated successfully.")
    @APIResponse(responseCode = "404", description = "Product not found.")
    public Uni<Product> update(@Parameter(description = "ID of the product to update") @PathParam("id") Long id, Product product) {
        return productService.update(id, product);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.")
    @APIResponse(responseCode = "200", description = "Product deleted successfully.")
    @APIResponse(responseCode = "404", description = "Product not found.")
    public Uni<Response> delete(@Parameter(description = "ID of the product to delete") @PathParam("id") Long id) {
        return productService.delete(id)
                .map(deleted -> deleted ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/check-stock/{id}/{count}")
    @Operation(summary = "Check product stock", description = "Checks if the requested quantity is available for a product.")
    @APIResponse(responseCode = "200", description = "Stock availability status.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Boolean.class)))
    @APIResponse(responseCode = "404", description = "Product not found.")
    public Uni<Response> checkStock(@Parameter(description = "ID of the product to check") @PathParam("id") Long id,
                                    @Parameter(description = "Quantity to check") @PathParam("count") int count) {
        return productService.checkStock(id, count)
                .map(available -> Response.ok(available).build());
    }

    @GET
    @Path("/sorted-by-price")
    @Operation(summary = "Get products sorted by price", description = "Retrieves all products sorted in ascending order by price.")
    @APIResponse(responseCode = "200", description = "List of products sorted by price.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Product.class)))
    public Uni<List<Product>> sortByPrice() {
        return productService.sortByPrice();
    }
}