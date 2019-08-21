package com.evanco.invoiceservice.dao;

import com.evanco.invoiceservice.model.InvoiceItem;

import java.util.List;

public interface InvoiceItemDao {

    InvoiceItem addInvoiceItem(InvoiceItem invoiceItem);

    InvoiceItem getInvoiceItem(int id);

    void updateInvoiceItem(InvoiceItem invoiceItem);

    void deleteInvoiceItem(int id);

    List<InvoiceItem> getAllInvoiceItems();

    List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId);
}
