package com.sunny.SpringDataJPADemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunny.SpringDataJPADemo.model.Product;
import com.sunny.SpringDataJPADemo.repository.ProductRepo;

/**
 * Service class handling the core business logic for Product operations.
 * Communicates directly with the Product repository.
 */
@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;

	/**
	 * Fetches all products entirely from the database.
	 * @return List of products.
	 */
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	/**
	 * Fetches a single product by its unique database ID.
	 * @param productId The ID of the product.
	 * @return The product if found, else an empty new Product instance.
	 */
	public Product getProductById(int productId) {
		return productRepo.findById(productId).orElse(new Product());
	}

	/**
	 * Saves a new product to the database.
	 * @param product The product containing unsaved changes.
	 * @return The saved product containing its new ID.
	 */
	public Product addProduct(Product product) {
		return productRepo.save(product);
	}

	/**
	 * Updates an existing product. 
	 * @param product The modified product data.
	 * @return The updated product reflecting database changes.
	 */
	public Product updateProduct(Product product) {
		return productRepo.save(product);
	}

	/**
	 * Attempts to delete a product securely by its ID.
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
