package com.sunny.SpringDataJPADemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunny.SpringDataJPADemo.model.Product;
import com.sunny.SpringDataJPADemo.repository.ProductRepo;

@Service
public class ProductService {

	@Autowired
	ProductRepo productRepo;

	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	public Product getProductById(int productId) {
		return productRepo.findById(productId).orElse(new Product());
	}

	public Product addProduct(Product product) {
		return productRepo.save(product);
	}

	public Product updateProduct(Product product) {
		return productRepo.save(product);
	}

	public String deleteProduct(int productId) {
		try {
			productRepo.deleteById(productId);
			return "Product successfully deleted";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
