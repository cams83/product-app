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

    @Operation(summary = "Create a new product")
    @PostMapping
    @ResponseStatus(code = CREATED)
    fun createProduct(@RequestBody product: Mono<@Valid Product>): Mono<Product> = product.flatMap { productService.createProduct(it) }

    @Operation(summary = "Update a product", parameters = [Parameter(name = "id", description = "Product id", required = true)])
    @PutMapping("/{id}")
    @ResponseStatus(code = OK)
    fun updateProduct(@PathVariable id: BigInteger, @RequestBody updatedProduct: Mono<@Valid Product>): Mono<Product> =
        updatedProduct.flatMap { productService.updateProduct(id, it) }

    @Operation(summary = "Delete a product", parameters = [Parameter(name = "id", description = "Product id", required = true)])
    @DeleteMapping("/{id}")
    @ResponseStatus(code =  NO_CONTENT)
    fun deleteProduct(@PathVariable id: BigInteger): Mono<Void> = productService.deleteProduct(id)
}