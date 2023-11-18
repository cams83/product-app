package com.cams7.product.service

import com.cams7.product.model.Product
import com.cams7.product.repository.ProductRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigInteger

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(): Flux<Product> = productRepository.findAll()

    fun getProductById(id: BigInteger): Mono<Product> = productRepository.findById(id)

    fun createProduct(product: Product): Mono<Product> = productRepository.save(product)

    fun updateProduct(id: BigInteger, updatedProduct: Product): Mono<Product> {
        return productRepository.findById(id)
            .flatMap {
                updatedProduct.id = it.id
                productRepository.save(updatedProduct)
            }
    }

    fun deleteProduct(id: BigInteger): Mono<Void> = productRepository.deleteById(id)
}