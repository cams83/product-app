package com.cams7.product.controller

import com.cams7.product.model.Product
import com.cams7.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigInteger


@Tag(name = "Product", description = "Product API")
@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {

    @Operation(summary = "Get all products")
    @GetMapping
    @ResponseStatus(code = OK)
    fun getAllProducts(): Flux<Product> = productService.getAllProducts()

    @Operation(summary = "Get a product by id", parameters = [Parameter(name = "id", description = "Product id", required = true)])
    @GetMapping("/{id}")
    @ResponseStatus(code = OK)
    fun getProductById(@PathVariable id: BigInteger): Mono<Product> = productService.getProductById(id)

    @Operation(summary = "Create a new product", parameters = [Parameter(name = "product", description = "Product to create", required = true)])
    @PostMapping
    @ResponseStatus(code = CREATED)
    fun createProduct(@RequestBody product: Product): Mono<Product> = productService.createProduct(product)

    @Operation(summary = "Update a product", parameters = [Parameter(name = "id", description = "Product id", required = true), Parameter(name = "product", description = "Product to update", required = true)])
    @PutMapping("/{id}")
    @ResponseStatus(code = OK)
    fun updateProduct(@PathVariable id: BigInteger, @RequestBody updatedProduct: Product): Mono<Product> =
        productService.updateProduct(id, updatedProduct)

    @Operation(summary = "Delete a product", parameters = [Parameter(name = "id", description = "Product id", required = true)])
    @DeleteMapping("/{id}")
    @ResponseStatus(code =  NO_CONTENT)
    fun deleteProduct(@PathVariable id: BigInteger): Mono<Void> = productService.deleteProduct(id)
}