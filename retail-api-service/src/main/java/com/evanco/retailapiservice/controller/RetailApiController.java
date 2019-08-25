package com.evanco.retailapiservice.controller;

import com.evanco.retailapiservice.model.*;
import com.evanco.retailapiservice.service.RetailApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RefreshScope
@CacheConfig(cacheNames = "retail")
public class RetailApiController {

    @Autowired
    private RetailApiService service;

    @Autowired
    public RetailApiController(RetailApiService service) {
        this.service = service;
    }

    // routes

    // didn't cache b/c result would change frequently as invoice points change
    // handles requests to get level up points by customer id
    @RequestMapping(value = "/levelup/customer/{id}", method = RequestMethod.GET)
    public int getLevelUp(@PathVariable("id") int id) {
        return service.getPoints(id);
    }

    // adds the return value of the method to the cache using invoice id as the key
    // handles requests to add an invoice
    @CachePut(key = "#result.getInvoiceId()")
    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    public InvoiceViewModelResponse addInvoice(@RequestBody InvoiceViewModel ivm) {
        return service.addInvoice(ivm);
    }

    // caches the result of the method - it automatically uses id as the key
    // handles requests to retrieve an invoice by invoice id
    @Cacheable
    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public InvoiceViewModel getInvoice(@PathVariable("id") int id){
        return service.getInvoice(id);
    }

    // didn't cache b/c result would change frequently as invoices are added
    // handles requests to retrieve all invoices
    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> getAllInvoices() {
        return service.getAllInvoices();
    }

    // didn't cache b/c result would change frequently as invoices are added
    // handles requests to retrieve all invoices by customer id
    @RequestMapping(value = "/invoices/customer/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<InvoiceViewModel> getInvoiceByCustomerId(@PathVariable("id") int id){
        return service.getInvoicesByCustomerId(id);
    }

    // didn't cache b/c result would change frequently as inventory is modified
    // handles requests to retrieve all inventory
    @RequestMapping(value = "/products/inventory", method = RequestMethod.GET)
    public List<Product> getProductsInInventory() {
        return service.getProductsInInventory();
    }

    // caches the result of the method - it automatically uses id as the key
    // handles requests to retrieve a product by product id
    @Cacheable
    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Product getProduct(@PathVariable int id) {
        return service.getProduct(id);
    }

    // caches the result of the method - it automatically uses id as the key
    // handles requests to retrieve a list of products by invoice id
    @Cacheable
    @RequestMapping(value = "/products/invoice/{id}", method = RequestMethod.GET)
    public List<Product> getProductsByInvoiceId(@PathVariable int id) {
        return service.getProductsByInvoiceId(id);
    }

}
