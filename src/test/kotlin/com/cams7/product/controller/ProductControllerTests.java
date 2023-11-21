package com.cams7.product.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.cams7.product.model.Product;
import com.cams7.product.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductControllerTests {

  static final String PRODUCTS_URI = "/api/products";

  @Container
  private static final MySQLContainer<?> mySQLContainer =
      new MySQLContainer<>("mysql:5.7.42-debian").withInitScript("schema.sql");

  @DynamicPropertySource
  static void mySQLProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.r2dbc.url", getR2dbcUrl(mySQLContainer::getJdbcUrl));
    registry.add("spring.r2dbc.username", mySQLContainer::getUsername);
    registry.add("spring.r2dbc.password", mySQLContainer::getPassword);
  }

  @BeforeAll
  static void beforeAll() {
    mySQLContainer.start();
  }

  @AfterAll
  static void afterAll() {
    mySQLContainer.stop();
  }

  @Test
  void testMySQLContainerIsRunning() {
    assertTrue(mySQLContainer.isRunning());
  }

  @Autowired private WebTestClient webTestClient;

  @Autowired private ProductRepository productRepository;

  @BeforeEach
  void setup(ApplicationContext context) {
    webTestClient = WebTestClient.bindToApplicationContext(context).build();
    productRepository.deleteAll().block();
  }

  @Test
  void testGetAllProducts() {
    final var newProduct = getNewProduct();
    final var savedProduct = productRepository.save(newProduct).block();

    webTestClient
        .get()
        .uri(PRODUCTS_URI)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(Product.class)
        .consumeWith(
            it -> {
              final var products = it.getResponseBody();
              assertEquals(1, products.size());
              final var product = products.get(0);
              assertNotNull(product);
              assertEquals(savedProduct.getId(), product.getId());
              assertEquals(newProduct.getName(), product.getName());
              assertEquals(newProduct.getCategory(), product.getCategory());
              assertEquals(newProduct.getCost().doubleValue(), product.getCost().doubleValue());
              assertEquals(newProduct.getPrice().doubleValue(), product.getPrice().doubleValue());
            });
  }

  @Test
  void testGetProductById() {
    final var newProduct = getNewProduct();
    final var savedProduct = productRepository.save(newProduct).block();

    webTestClient
        .get()
        .uri(String.format("%s/%d", PRODUCTS_URI, savedProduct.getId()))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Product.class)
        .consumeWith(
            it -> {
              final var product = it.getResponseBody();
              assertNotNull(product);
              assertEquals(savedProduct.getId(), product.getId());
              assertEquals(newProduct.getName(), product.getName());
              assertEquals(newProduct.getCategory(), product.getCategory());
              assertEquals(newProduct.getCost().doubleValue(), product.getCost().doubleValue());
              assertEquals(newProduct.getPrice().doubleValue(), product.getPrice().doubleValue());
            });
  }

  @Test
  void testCreateProduct() {
    final var newProduct = getNewProduct();

    webTestClient
        .post()
        .uri(PRODUCTS_URI)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(newProduct)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Product.class)
        .consumeWith(
            it -> {
              final var createdProduct = it.getResponseBody();
              assertNotNull(createdProduct);
              assertNotNull(createdProduct.getId());
              assertEquals(newProduct.getName(), createdProduct.getName());
              assertEquals(newProduct.getCategory(), createdProduct.getCategory());
              assertEquals(
                  newProduct.getCost().doubleValue(), createdProduct.getCost().doubleValue());
              assertEquals(
                  newProduct.getPrice().doubleValue(), createdProduct.getPrice().doubleValue());
            });
  }

  @Test
  void testUpdateProduct() {
    final var newProduct = getNewProduct();
    final var updatedProduct =
        new Product(
            null,
            "Another Product",
            "Another Category",
            BigDecimal.valueOf(5.0),
            BigDecimal.valueOf(15.0));
    final var savedProduct = productRepository.save(newProduct).block();

    webTestClient
        .put()
        .uri(String.format("%s/%d", PRODUCTS_URI, savedProduct.getId()))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(updatedProduct)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Product.class)
        .consumeWith(
            it -> {
              final var product = it.getResponseBody();
              assertNotNull(product);
              assertEquals(savedProduct.getId(), product.getId());
              assertEquals(updatedProduct.getName(), product.getName());
              assertEquals(updatedProduct.getCategory(), product.getCategory());
              assertEquals(updatedProduct.getCost().doubleValue(), product.getCost().doubleValue());
              assertEquals(
                  updatedProduct.getPrice().doubleValue(), product.getPrice().doubleValue());
            });
  }

  @Test
  void testDeleteProduct() {
    final var newProduct = getNewProduct();
    final var savedProduct = productRepository.save(newProduct).block();

    webTestClient
        .delete()
        .uri(String.format("%s/%d", PRODUCTS_URI, savedProduct.getId()))
        .exchange()
        .expectStatus()
        .isNoContent();
  }

  private static Product getNewProduct() {
    return new Product(
        null, "Some Product", "Some Category", BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0));
  }

  private static Supplier<Object> getR2dbcUrl(final Supplier<String> jdbcUrl) {
    if (StringUtils.isBlank(jdbcUrl.get())) return () -> "";

    return () -> jdbcUrl.get().replaceFirst("jdbc", "r2dbc");
  }
}
