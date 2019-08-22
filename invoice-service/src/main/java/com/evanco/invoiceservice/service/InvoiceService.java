package com.evanco.invoiceservice.service;

import com.evanco.invoiceservice.dao.InvoiceDao;
import com.evanco.invoiceservice.dao.InvoiceItemDao;
import com.evanco.invoiceservice.model.Invoice;
import com.evanco.invoiceservice.model.InvoiceItem;
import com.evanco.invoiceservice.model.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceService {
    // Field injection
    InvoiceDao invoiceDao;
    InvoiceItemDao invoiceItemDao;

    // Constructor injection
    @Autowired
    public InvoiceService(InvoiceDao invoiceDao, InvoiceItemDao invoiceItemDao) {
        this.invoiceDao = invoiceDao;
        this.invoiceItemDao = invoiceItemDao;
    }

    // Service methods
    // add invoice
    @Transactional
    public InvoiceViewModel addInvoice(InvoiceViewModel ivm) {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(ivm.getCustomerId());
        invoice.setPurchaseDate(ivm.getPurchaseDate());
        invoice = invoiceDao.addInvoice(invoice);

        for(InvoiceItem ii: ivm.getInvoiceItems()){
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setQuantity(ii.getQuantity());
            invoiceItem.setInvoiceId(invoice.getInvoiceId());
            invoiceItem.setUnitPrice(ii.getUnitPrice());
            invoiceItem.setInventoryId(ii.getInventoryId());
            invoiceItemDao.addInvoiceItem(invoiceItem);
        }
        return buildInvoiceViewModel(invoice);
    }

    // get invoice
    public InvoiceViewModel getInvoice(int id) {
        return buildInvoiceViewModel(invoiceDao.getInvoice(id));
    }

    // update invoice
    public void updateInvoice(InvoiceViewModel ivm, int id) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(id);
        invoice.setCustomerId(ivm.getCustomerId());
        invoice.setPurchaseDate(ivm.getPurchaseDate());
        invoiceDao.updateInvoice(invoice);
    }

    // delete invoice
    public void deleteInvoice(int id) {
        // handle fk constraints
        invoiceItemDao.getInvoiceItemsByInvoiceId(id)
                .forEach(ii -> invoiceItemDao.deleteInvoiceItem(ii.getInvoiceItemId()));
        invoiceDao.deleteInvoice(id);
    }

    // get all invoices
    public List<InvoiceViewModel> getAllInvoices() {
        List<InvoiceViewModel> ivms = new ArrayList<>();
        for (Invoice i : invoiceDao.getAllInvoices()) {
            ivms.add(buildInvoiceViewModel(i));
        }
        return ivms;
    }
    // get invoices by customer id
    public List<InvoiceViewModel> getInvoiceByCustomerId(int id) {
        List<InvoiceViewModel> ivms = new ArrayList<>();
        for (Invoice i : invoiceDao.getInvoicesByCustomerId(id)) {
            ivms.add(buildInvoiceViewModel(i));
        }
        return ivms;
    }


    // BuildViewModel helper methods
    private InvoiceViewModel buildInvoiceViewModel(Invoice invoice) {
        InvoiceViewModel ivm = new InvoiceViewModel();
        ivm.setInvoiceId(invoice.getInvoiceId());
        ivm.setCustomerId(invoice.getCustomerId());
        ivm.setPurchaseDate(invoice.getPurchaseDate());
        ivm.setInvoiceItems(invoiceItemDao.getInvoiceItemsByInvoiceId(invoice.getInvoiceId()));
        return ivm;
    }
}
