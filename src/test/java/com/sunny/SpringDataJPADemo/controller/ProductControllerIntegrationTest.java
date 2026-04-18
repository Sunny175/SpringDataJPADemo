package com.sunny.SpringDataJPADemo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunny.SpringDataJPADemo.dto.ProductDTO;
import com.sunny.SpringDataJPADemo.repository.ProductRepo;

/**
 * Full Database Integration Test.
 * Bootstraps a temporary MySQL Docker container for complete testing isolation.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class ProductControllerIntegrationTest {

    // Bootstraps a disposable MySQL Docker instance before tests run
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    // Injects the disposable docker DB bindings dynamically into Spring Boot
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepo.deleteAll();
    }

    @Test
    void testAddAndRetrieveProduct() throws Exception {
        // 1. Prepare valid DTO payload
        ProductDTO newProduct = new ProductDTO();
        newProduct.setProductName("Docker Test Product");
        newProduct.setProductPrice(500);

        // 2. Perform POST to test the full pipeline (Validation -> DTO Mapping -> DB Save)
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("Docker Test Product")))
                .andExpect(jsonPath("$.productId").exists());

        // 3. Perform GET to verify it was actually saved in the DB and Pagination is working
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].productPrice", is(500)));
    }

    @Test
    void testValidationFailure() throws Exception {
        // Send a bad payload (negative price) to hit the GlobalExceptionHandler
        ProductDTO badProduct = new ProductDTO();
        badProduct.setProductName("Invalid Tool");
        badProduct.setProductPrice(-100); 

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(badProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.productPrice").exists());
    }
}
