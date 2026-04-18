package com.sunny.SpringDataJPADemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	 * Retrieves all products.
	 * @return A list of all stored products.
	 */
	@GetMapping(value = "/products", produces = "application/json")
	public List<Product> getProducts() {
		return productService.getAllProducts();
	}

	@GetMapping(value = "/products/{productId}", produces = "application/json")
	public Product getProductById(@PathVariable int productId) {
		return productService.getProductById(productId);
	}

	/**
	 * Adds a new product to the database after validating fields.
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
