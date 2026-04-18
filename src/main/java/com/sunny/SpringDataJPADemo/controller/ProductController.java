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

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	private ProductService productService;

	@GetMapping(value = "/products", produces = "application/json")
	public List<Product> getProducts() {
		return productService.getAllProducts();
	}

	@GetMapping(value = "/products/{productId}", produces = "application/json")
	public Product getProductById(@PathVariable int productId) {
		return productService.getProductById(productId);
	}

	@PostMapping(value = "/products", consumes = "application/json", produces = "application/json")
	public Product addProduct(@RequestBody Product product) {
		return productService.addProduct(product);
	}

	@PatchMapping(value = "/products/{productId}", consumes = "application/json", produces = "application/json")
	public Product updateProduct(@RequestBody Product product) {
		return productService.updateProduct(product);

	}

	@DeleteMapping(value = "/products/{productId}")
	public String deleteProduct(@PathVariable int productId) {
		return productService.deleteProduct(productId);
	}

}
