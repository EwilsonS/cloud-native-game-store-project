package com.company.adminapiservice.controller;

import com.company.adminapiservice.service.InvoiceService;
import com.company.adminapiservice.util.messages.Invoice;
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
@RequestMapping("/invoices")
@CacheConfig(cacheNames = {"invoices"})
public class InvoiceController {

    @Autowired
    InvoiceService service;

    // adds the return value of the method to the cache using invoice id as the key
    // handles requests to add an invoice
    @CachePut(key = "#result.getInvoiceId()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice addInvoice(@RequestBody Invoice ivm){
        return service.addInvoice(ivm);
    }

    // didn't cache b/c result would change frequently as invoices are added
    // handles requests to retrieve all invoices
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getAllInvoices(){
        return service.getAllInvoices();
    }

    // caches the result of the method - it automatically uses id as the key
    // handles requests to retrieve an invoice by invoice id
    @Cacheable
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice getInvoice(@PathVariable("id") int id){
        return service.getInvoice(id);
    }

    // removes invoice with given invoice id as the key from the cache
    // handles requests to update an invoice with a matching id
    @CacheEvict(key = "#ivm.getInvoiceId()")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInvoice(@RequestBody @Valid Invoice ivm, @PathVariable("id") int id){
        if (ivm.getInvoiceId() == 0)
            ivm.setInvoiceId(id);
        if (id != ivm.getInvoiceId()) {
            throw new IllegalArgumentException("ID on path must match the ID in the Invoice object");
        }
        service.updateInvoice(ivm, id);
    }

    // removes invoice with given invoice id as the key from the cache
    // handles requests to delete an invoice by id
    @CacheEvict
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable("id") int id){
        service.deleteInvoice(id);
    }

    // didn't cache b/c result would change frequently as invoices are added
    // handles requests to retrieve all invoices by customer id
    @GetMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getInvoiceByCustomerId(@PathVariable("id") int id){
        return service.getInvoicesByCustomerId(id);
    }

}
