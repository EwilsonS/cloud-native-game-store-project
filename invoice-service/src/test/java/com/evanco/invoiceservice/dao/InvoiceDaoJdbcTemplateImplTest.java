package com.evanco.invoiceservice.dao;

import com.evanco.invoiceservice.exception.NotFoundException;
import com.evanco.invoiceservice.model.Invoice;
import com.evanco.invoiceservice.model.InvoiceItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceDaoJdbcTemplateImplTest {

    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private InvoiceItemDao invoiceItemDao;

    @Before
    public void setUp() throws Exception {
        invoiceItemDao.getAllInvoiceItems()
                .stream()
                .forEach(ii -> invoiceItemDao.deleteInvoiceItem(ii.getInvoiceItemId()));
        invoiceDao.getAllInvoices()
                .stream()
                .forEach(i->invoiceDao.deleteInvoice(i.getInvoiceId()));
    }

    @Test
    public void addGetDeleteInvoice() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);

        // add
        invoice = invoiceDao.addInvoice(invoice);

        // get
        Invoice invoice1 = invoiceDao.getInvoice(invoice.getInvoiceId());
        assertEquals(invoice, invoice1);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        // delete
        invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId());
        invoiceDao.deleteInvoice(invoice.getInvoiceId());
        assertEquals(0, invoiceDao.getAllInvoices().size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void deleteInvoiceWithoutCascading() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);

        // add
        invoice = invoiceDao.addInvoice(invoice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        // delete without cascading should result in exception
        invoiceDao.deleteInvoice(invoice.getInvoiceId());
        assertEquals(0, invoiceDao.getAllInvoices().size());
    }

    @Test
    public void updateInvoice() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);

        // add
        invoice = invoiceDao.addInvoice(invoice);

        //update
        invoice.setPurchaseDate(LocalDate.of(1987, 1, 26));
        invoiceDao.updateInvoice(invoice);
        assertEquals(LocalDate.of(1987, 1, 26), invoiceDao.getInvoice(invoice.getInvoiceId()).getPurchaseDate());
    }

    @Test
    public void getAllInvoices() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);

        // add 2 invoices
        invoiceDao.addInvoice(invoice);
        invoiceDao.addInvoice(invoice);

        // get all
        assertEquals(2, invoiceDao.getAllInvoices().size());
    }

    @Test
    public void getInvoicesByCustomerId() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoiceDao.addInvoice(invoice);

        Invoice invoice2 = new Invoice();
        invoice2.setCustomerId(1);
        invoiceDao.addInvoice(invoice2);

        Invoice invoice3 = new Invoice();
        invoice2.setCustomerId(2);
        invoiceDao.addInvoice(invoice2);

        // get all
        assertEquals(3, invoiceDao.getAllInvoices().size());

        // get by customer id
        assertEquals(2, invoiceDao.getInvoicesByCustomerId(1).size());

    }

    @Test
    public void getInvoiceWithNullId(){
        assertNull(invoiceDao.getInvoice(9999));
    }

    @Test(expected = NotFoundException.class)
    public void deleteInvoiceWithNullId(){
        Invoice invoice = new Invoice();
        invoiceDao.deleteInvoice(invoice.getInvoiceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateInvoiceWithNullId(){
        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);

        // add without binding id
        invoiceDao.addInvoice(invoice);

        //update
        invoice.setInvoiceId(999);
        invoice.setPurchaseDate(LocalDate.of(1987, 1, 26));
        invoiceDao.updateInvoice(invoice);
        assertEquals(LocalDate.of(1987, 1, 26), invoiceDao.getInvoice(invoice.getInvoiceId()).getPurchaseDate());

    }
}