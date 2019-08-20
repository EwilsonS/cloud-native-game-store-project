package com.evanco.invoiceservice.dao;

import com.evanco.invoiceservice.model.Invoice;

import java.util.List;

public interface InvoiceDao {
    Invoice addInvoice(Invoice invoice);

    Invoice getInvoice(int id);

    void updateInvoice(Invoice invoice);

    void deleteInvoice(int id);

    List<Invoice> getAllInvoices();

    List<Invoice> getInvoicesByCustomerId(int customerId);
}
