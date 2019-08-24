package com.company.adminapiservice.util.feign;

import com.company.adminapiservice.util.messages.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerClient {

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    Customer addCustomer(@RequestBody @Valid Customer customer);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
    Customer getCustomer(@PathVariable int id);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    void updateCustomer(@PathVariable int id, @RequestBody @Valid Customer customer);

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    void deleteCustomer(@PathVariable int id);

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    List<Customer> getAllCustomers();

}
