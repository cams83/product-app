package com.cams7.product.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.math.BigInteger

@Table(name = "products")
data class Product(
    @Schema(description = "Product id", example = "1")
    @Id
    var id: BigInteger? = null,

    @Schema(description = "Product name", example = "Product 1")
    @NotBlank
    val name: String,

    @Schema(description = "Product category", example = "Category 1")
    @NotBlank
    val category: String,

    @Schema(description = "Product cost", example = "10.00")
    @NotNull
    @Min(0)
    val cost: BigDecimal,

    @Schema(description = "Product price", example = "21.99")
    @NotNull
    @Min(0)
    val price: BigDecimal
)