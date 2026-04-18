package com.sunny.SpringDataJPADemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.sunny.SpringDataJPADemo.dto.ProductDTO;
import com.sunny.SpringDataJPADemo.mapper.ProductMapper;
import com.sunny.SpringDataJPADemo.model.Product;
import com.sunny.SpringDataJPADemo.service.ProductService;

import jakarta.validation.Valid;

/**
 * REST controller for managing product operations.
 * Now using DTOs to separate the API from the Database Entity.
 */
@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService productService;

	// Inject our MapStruct mapper
	@Autowired
	private ProductMapper productMapper;

	/**
	 * Retrieves products using DTOs to hide internal entity details.
	 */
	@GetMapping(value = "/products", produces = "application/json")
	public Page<ProductDTO> getProducts(
			@RequestParam(required = false) String keyword,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "productId") String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

		// The .map() function safely loops over the Page and converts every Product
		// into a ProductDTO!
		return productService.getAllProducts(keyword, pageable).map(productMapper::toDto);
	}

	@GetMapping(value = "/products/{productId}", produces = "application/json")
	public ProductDTO getProductById(@PathVariable int productId) {
		Product product = productService.getProductById(productId);
		return productMapper.toDto(product);
	}

	@PostMapping(value = "/products", consumes = "application/json", produces = "application/json")
	public ProductDTO addProduct(@Valid @RequestBody ProductDTO productDTO) {
		// 1. Convert incoming API DTO -> DB Entity
		Product entity = productMapper.toEntity(productDTO);
		// 2. Save DB Entity
		Product saved = productService.addProduct(entity);
		// 3. Convert saved DB Entity -> Output DTO
		return productMapper.toDto(saved);
	}

	@PatchMapping(value = "/products/{productId}", consumes = "application/json", produces = "application/json")
	public ProductDTO updateProduct(@Valid @RequestBody ProductDTO productDTO) {
		Product entity = productMapper.toEntity(productDTO);
		Product updated = productService.updateProduct(entity);
		return productMapper.toDto(updated);
	}

	@DeleteMapping(value = "/products/{productId}")
	public String deleteProduct(@PathVariable int productId) {
		return productService.deleteProduct(productId);
	}
}
