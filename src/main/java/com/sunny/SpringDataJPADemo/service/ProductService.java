package com.sunny.SpringDataJPADemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sunny.SpringDataJPADemo.model.Product;
import com.sunny.SpringDataJPADemo.repository.ProductRepo;

/**
 * Service class handling the core business logic for Product operations.
 * Communicates directly with the Product repository.
 */
@Service
public class ProductService {

	private Specification<Product> buildSearchSpecification(String keyword) {
		return (root, query, criteriaBuilder) -> {
			if (!StringUtils.hasText(keyword)) {
				return null;
			}

			return criteriaBuilder.like(
					criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%");
		};
	}

	@Autowired
	ProductRepo productRepo;

	/**
	 * Fetches products using dynamic search filters and pagination.
	 * 
	 * @param keyword  The search term for product names.
	 * @param pageable The pagination and sorting instructions.
	 * @return A Page containing a slice of matching products.
	 */
	public Page<Product> getAllProducts(String keyword, Pageable pageable) {
		Specification<Product> spec = buildSearchSpecification(keyword);
		return productRepo.findAll(spec, pageable);
	}

	/**
	 * Fetches a single product by its unique database ID.
	 * 
	 * @param productId The ID of the product.
	 * @return The product if found, else an empty new Product instance.
	 */
	public Product getProductById(int productId) {
		return productRepo.findById(productId).orElse(new Product());
	}

	/**
	 * Saves a new product to the database.
	 * 
	 * @param product The product containing unsaved changes.
	 * @return The saved product containing its new ID.
	 */
	public Product addProduct(Product product) {
		return productRepo.save(product);
	}

	/**
	 * Updates an existing product.
	 * 
	 * @param product The modified product data.
	 * @return The updated product reflecting database changes.
	 */
	public Product updateProduct(Product product) {
		return productRepo.save(product);
	}

	/**
	 * Attempts to delete a product securely by its ID.
	 * 
	 * @param productId The ID of the product to terminate.
	 * @return A custom success or failure message.
	 */
	public String deleteProduct(int productId) {
		try {
			productRepo.deleteById(productId);
			return "Product successfully deleted";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
