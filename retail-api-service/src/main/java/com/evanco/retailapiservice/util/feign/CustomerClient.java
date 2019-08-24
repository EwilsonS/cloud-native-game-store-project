package com.evanco.retailapiservice.util.feign;

import com.evanco.retailapiservice.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    // get customer by id to verify existence
    @GetMapping(value = "/customers/{id}")
    Customer getCustomer(@PathVariable("id") int id);
}
