package com.evanco.retailapiservice.util.feign;

import com.evanco.retailapiservice.model.InvoiceViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "invoice-service")
public interface InvoiceClient {

    // add invoice
    @PostMapping(value = "/invoices")
    InvoiceViewModel addInvoice(@RequestBody InvoiceViewModel ivm);

    // get all invoices
    @GetMapping(value = "/invoices")
    List<InvoiceViewModel> getAllInvoices();

    // get invoice
    @GetMapping(value = "/invoices/{id}")
    InvoiceViewModel getInvoice(@PathVariable("id") int id);

    // get invoices by customer id
    @GetMapping(value = "/invoices/customer/{id}")
    List<InvoiceViewModel> getInvoicesByCustomerId(@PathVariable("id") int id);

}
