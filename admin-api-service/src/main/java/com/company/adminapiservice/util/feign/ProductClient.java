package com.company.adminapiservice.util.feign;

import com.company.adminapiservice.util.messages.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    Product addProduct(@RequestBody @Valid Product product);

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    Product getProduct(@PathVariable int id);

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    void updateProduct(@PathVariable int id, @RequestBody @Valid Product product);

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    void deleteProduct(@PathVariable int id);

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    List<Product> getAllProducts();

}
