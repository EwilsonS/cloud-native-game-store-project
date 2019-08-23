package com.evanco.invoiceservice.dao;

import com.evanco.invoiceservice.model.Invoice;

import java.util.List;

public interface InvoiceDao {

    // standard CRUD

    Invoice addInvoice(Invoice invoice);

    Invoice getInvoice(int id);

    void updateInvoice(Invoice invoice);

    void deleteInvoice(int id);

    List<Invoice> getAllInvoices();

    // additional methods

    List<Invoice> getInvoicesByCustomerId(int customerId);
}
