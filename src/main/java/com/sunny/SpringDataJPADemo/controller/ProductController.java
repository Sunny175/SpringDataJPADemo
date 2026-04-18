package com.sunny.SpringDataJPADemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunny.SpringDataJPADemo.model.Product;
import com.sunny.SpringDataJPADemo.service.ProductService;

import jakarta.validation.Valid;

/**
 * REST controller for managing product operations.
 * Exposes API endpoints mapping to /api/products.
 */
@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	private ProductService productService;

	/**
	 * Retrieves products with optional search, pagination, and sorting.
	 * 
	 * @param keyword The term to search for (optional).
	 * @param page    The page number to fetch (starts at 0).
	 * @param size    The number of items per page.
	 * @param sortBy  The field to sort the results by.
	 * @return A Page of products.
	 */
	@GetMapping(value = "/products", produces = "application/json")
	public Page<Product> getProducts(
			@RequestParam(required = false) String keyword,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "productId") String sortBy

	) {
		// Constructs the Pageable request telling the database exactly what slice of
		// data we want
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return productService.getAllProducts(keyword, pageable);
	}

	@GetMapping(value = "/products/{productId}", produces = "application/json")
	public Product getProductById(@PathVariable int productId) {
		return productService.getProductById(productId);
	}

	/**
	 * Adds a new product to the database after validating fields.
	 * 
	 * @param product The product object passed in the request body.
	 * @return The saved product.
	 */
	@PostMapping(value = "/products", consumes = "application/json", produces = "application/json")
	public Product addProduct(@Valid @RequestBody Product product) {
		return productService.addProduct(product);
	}

	@PatchMapping(value = "/products/{productId}", consumes = "application/json", produces = "application/json")
	public Product updateProduct(@Valid @RequestBody Product product) {
		return productService.updateProduct(product);

	}

	@DeleteMapping(value = "/products/{productId}")
	public String deleteProduct(@PathVariable int productId) {
		return productService.deleteProduct(productId);
	}

}
