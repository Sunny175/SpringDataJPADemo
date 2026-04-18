package com.sunny.SpringDataJPADemo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.sunny.SpringDataJPADemo.model.Product;
import com.sunny.SpringDataJPADemo.repository.ProductRepo;

/**
 * Unit tests for ProductService.
 * Uses Mockito to mock the database repository layer.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setProductId(1);
        mockProduct.setProductName("Test Product");
        mockProduct.setProductPrice(100);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> mockPage = new PageImpl<>(Collections.singletonList(mockProduct));
        // Using any() for Specification and eq() for pageable since spec is built dynamically
        when(productRepo.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);

        // Act
        Page<Product> result = productService.getAllProducts("Test", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getProductName());
    }

    @Test
    void testGetProductById_Found() {
        when(productRepo.findById(1)).thenReturn(Optional.of(mockProduct));

        Product result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals(1, result.getProductId());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepo.findById(99)).thenReturn(Optional.empty());

        Product result = productService.getProductById(99);

        assertNotNull(result);
        assertEquals(0, result.getProductId()); // our service returns new Product() fallback
    }

    @Test
    void testAddProduct() {
        when(productRepo.save(mockProduct)).thenReturn(mockProduct);

        Product result = productService.addProduct(mockProduct);

        assertNotNull(result);
        assertEquals("Test Product", result.getProductName());
        verify(productRepo).save(mockProduct);
    }

    @Test
    void testDeleteProduct() {
        String result = productService.deleteProduct(1);
        
        assertEquals("Product successfully deleted", result);
        verify(productRepo).deleteById(1);
    }
}
