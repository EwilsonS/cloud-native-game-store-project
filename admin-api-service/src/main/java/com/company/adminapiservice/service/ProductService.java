package com.company.adminapiservice.service;

import com.company.adminapiservice.util.messages.Product;
import com.company.adminapiservice.util.feign.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductService {

    @Autowired
    private ProductClient productClient;

    // constructors

    public ProductService() {
    }

    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    // methods

    public Product addProduct(Product product) {
        return productClient.addProduct(product);
    }

    public Product getProduct(int id) {
        return productClient.getProduct(id);
    }

    public void updateProduct(int id, Product product) {
        productClient.updateProduct(id, product);
    }

    public void deleteProduct(int id) {
        productClient.deleteProduct(id);
    }

    public List<Product> getAllProducts() {
        return productClient.getAllProducts();
    }

}