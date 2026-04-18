package com.sunny.SpringDataJPADemo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProductDTO {

    private int productId;

    @NotBlank(message = "Product name can not be empty")
    private String productName;

    @Min(value = 0, message = "Product price must be greater than or equal to 0")
    private int productPrice;

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
}
