package com.evanco.retailapiservice.controller;

import com.evanco.retailapiservice.model.Invoice;
import com.evanco.retailapiservice.model.InvoiceViewModel;
import com.evanco.retailapiservice.model.InvoiceViewModelResponse;
import com.evanco.retailapiservice.model.LevelUp;
import com.evanco.retailapiservice.service.RetailApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
//@CacheConfig(cacheNames = "retail")
public class RetailApiController {

    @Autowired
    private RetailApiService service;

    @Autowired
    public RetailApiController(RetailApiService service) {
        this.service = service;
    }
    @RequestMapping("/levelups/{id}")
    public LevelUp getLevelUp(@PathVariable("id") int id) {
        return service.getLevelUpInfo(id);
    }

    @PostMapping(value = "/invoices")
    public InvoiceViewModelResponse addInvoice(@RequestBody InvoiceViewModel ivm) {
        return service.addInvoice(ivm);
    }

//@GetMapping(value = "/invoices")

//@GetMapping(value = "/invoices/{id}")

//@GetMapping(value= "/invoices/customer/{id}")

//@GetMapping(value="/products/inventory")

//@GetMapping(value="/products/{id}")

//@GetMapping(value="/products/invoice/{id}")

//@GetMapping(value="/levelups/customer/{id}")

}
