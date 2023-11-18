package com.cams7.product.repository

import com.cams7.product.model.Product
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import java.math.BigInteger

interface ProductRepository : R2dbcRepository<Product, BigInteger>