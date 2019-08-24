package com.company.adminapiservice.util.feign;

import com.company.adminapiservice.util.messages.Invoice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "invoice-service")
public interface InvoiceClient {

    @RequestMapping(value = "/invoices", method = RequestMethod.POST)
    Invoice addInvoice(@RequestBody @Valid Invoice ivm);

    @RequestMapping(value = "/invoices", method = RequestMethod.GET)
    List<Invoice> getAllInvoices();

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
    Invoice getInvoice(@PathVariable("id") int id);

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.PUT)
    void updateInvoice(@RequestBody @Valid Invoice ivm, @PathVariable("id") int id);

    @RequestMapping(value = "/invoices/{id}", method = RequestMethod.DELETE)
    void deleteInvoice(@PathVariable("id") int id);

    @RequestMapping(value = "/invoices//customer/{id}", method = RequestMethod.GET)
    List<Invoice> getInvoiceByCustomerId(@PathVariable("id") int id);
}
