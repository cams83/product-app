package com.cams7.product

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(title = "Product API", version = "1.0", description = "Product API")
)
@EnableR2dbcRepositories
@SpringBootApplication
class ProductApplication

fun main(args: Array<String>) {
    runApplication<ProductApplication>(*args)
}

