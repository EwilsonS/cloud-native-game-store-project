package com.company.adminapiservice.controller;

import com.company.adminapiservice.util.messages.Product;
import com.company.adminapiservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = {"products"})
public class ProductController {

    @Autowired
    ProductService productService;

    // constructors

    public ProductController() {
    }

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Product Routes

    // adds the return value of the method to the cache using product id as the key
    // handles requests to add a product
    @CachePut(key = "#result.getProductId()")
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody @Valid Product product) {
        return productService.addProduct(product);
    }

    // caches the result of the method - it automatically uses id as the key
    // handles requests to retrieve a product by product id
    @Cacheable
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable int id) {
        Product product = productService.getProduct(id);
        return product;
    }

    // removes product with given product id as the key from the cache
    // handles requests to update a product with a matching id
    @CacheEvict(key = "#product.getProductId()")
    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable int id, @RequestBody @Valid Product product) {
        if (product.getProductId() == 0)
            product.setProductId(id);
        if (id != product.getProductId()) {
            throw new IllegalArgumentException("ID on path must match the ID in the Product object");
        }
        productService.updateProduct(id, product);
    }

    // removes product with given product id as the key from the cache
    // handles requests to delete a product by id
    @CacheEvict
    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
    }

    // didn't cache b/c result would change frequently as products are added
    // handles requests to retrieve all products
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

}
