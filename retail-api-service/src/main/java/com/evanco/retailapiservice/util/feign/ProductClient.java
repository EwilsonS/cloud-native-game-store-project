package com.evanco.retailapiservice.util.feign;

import com.evanco.retailapiservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    // get all products
    @GetMapping(value = "/products")
    List<Product> getAllProducts();

    // get product by id
    @GetMapping(value = "/products/{id}")
    Product getProduct(@PathVariable("id") int id);

    // get products by invoice id
    @GetMapping(value = "/products/invoice/{id}")
    List<Product> getProductByInvoice(@PathVariable("id") int id);

}
