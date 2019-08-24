package com.company.adminapiservice.service;

import com.company.adminapiservice.util.feign.InvoiceClient;
import com.company.adminapiservice.util.messages.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceService {

    InvoiceClient invoiceClient;

    // constructors

    public InvoiceService() {
    }

    @Autowired
    public InvoiceService(InvoiceClient invoiceClient) {
        this.invoiceClient = invoiceClient;
    }

    // methods

    public Invoice addInvoice(Invoice ivm) {
        return invoiceClient.addInvoice(ivm);
    }

    public Invoice getInvoice(int id) {
        return invoiceClient.getInvoice(id);
    }

    public void updateInvoice(Invoice ivm, int id) {
        invoiceClient.updateInvoice(ivm, id);
    }

    public void deleteInvoice(int id) {
        invoiceClient.deleteInvoice(id);
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = invoiceClient.getAllInvoices();
            return invoiceClient.getAllInvoices();
    }

    public List<Invoice> getInvoicesByCustomerId(int id) {
        return invoiceClient.getInvoiceByCustomerId(id);
    }

}
