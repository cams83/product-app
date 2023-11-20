package com.cams7.product.service

import com.cams7.product.model.Product
import com.cams7.product.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.BigInteger

@ExtendWith(MockitoExtension::class)
class ProductServiceTests {
    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var productService: ProductService

    @Test
    fun testGetAllProducts() {
        val mockProducts = listOf(
            Product(BigInteger.valueOf(1L), "Product1", "Category1", BigDecimal.valueOf( 10.0), BigDecimal.valueOf(20.0)),
            Product(BigInteger.valueOf(2L), "Product2", "Category2", BigDecimal.valueOf(15.0), BigDecimal.valueOf(25.0))
        )

        given(productRepository.findAll()).willReturn(Flux.fromIterable(mockProducts))

        val result = productService.getAllProducts().collectList().block()

        assertNotNull(result)
        assertEquals(2, result!!.size)
        assertEquals("Product1", result[0].name)
        assertEquals("Product2", result[1].name)
    }

    @Test
    fun testGetProductById() {
        val productId = BigInteger.valueOf(1L)
        val mockProduct = Product(productId, "Product1", "Category1", BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0))

        given(productRepository.findById(productId)).willReturn(Mono.just(mockProduct))

        val result = productService.getProductById(productId).block()

        assertNotNull(result)
        assertEquals("Product1", result?.name)
    }

    @Test
    fun testCreateProduct() {
        val mockProduct = Product(null, "NewProduct", "NewCategory", BigDecimal.valueOf(30.0), BigDecimal.valueOf(40.0))
        val savedProduct = mockProduct.copy(id = BigInteger.valueOf(1L))

        given(productRepository.save(mockProduct)).willReturn(Mono.just(savedProduct))

        val result = productService.createProduct(mockProduct).block()

        assertNotNull(result)
        assertEquals(BigInteger.valueOf(1L), result?.id)
        assertEquals("NewProduct", result?.name )
    }

    @Test
    fun testUpdateProduct() {
        val productId = BigInteger.valueOf(1L)
        val mockProduct = Product(productId, "Product1", "Category1", BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0))
        val updatedProduct = mockProduct.copy(name = "UpdatedProduct")

        given(productRepository.findById(productId)).willReturn(Mono.just(mockProduct))
        given(productRepository.save(updatedProduct)).willReturn(Mono.just(updatedProduct))

        val result = productService.updateProduct(productId, updatedProduct).block()

        assertNotNull(result)
        assertEquals("UpdatedProduct", result?.name)
    }

    @Test
    fun testDeleteProduct() {
        val productId = BigInteger.valueOf(1L)

        given(productRepository.deleteById(productId)).willReturn(Mono.empty())

        val result = productService.deleteProduct(productId).block()

        assertNull(result)
    }
}