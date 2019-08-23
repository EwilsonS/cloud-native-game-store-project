package com.evanco.invoiceservice.dao;

import com.evanco.invoiceservice.exception.NotFoundException;
import com.evanco.invoiceservice.model.Invoice;
import com.evanco.invoiceservice.model.InvoiceItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvoiceItemDaoJdbcTemplateImplTest {

    @Autowired
    private InvoiceItemDao invoiceItemDao;
    @Autowired
    private InvoiceDao invoiceDao;

    @Before
    public void setUp() throws Exception {
        invoiceItemDao.getAllInvoiceItems()
                .stream()
                .forEach(ii -> invoiceItemDao.deleteInvoiceItem(ii.getInvoiceItemId()));
        invoiceDao.getAllInvoices()
                .stream()
                .forEach(i -> invoiceDao.deleteInvoice(i.getInvoiceId()));
    }

    @Test
    public void addGetDeleteInvoiceItem() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(10);
        invoice = invoiceDao.addInvoice(invoice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        // add
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        // get
        InvoiceItem invoiceItem1 = invoiceItemDao.getInvoiceItem(invoiceItem.getInvoiceItemId());
        assertEquals(invoiceItem, invoiceItem1);

        // delete
        invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId());
        assertEquals(0, invoiceItemDao.getAllInvoiceItems().size());
    }

    @Test
    public void updateInvoiceItem() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(10);
        invoice = invoiceDao.addInvoice(invoice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        // add
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);

        // update
        invoiceItem.setQuantity(99);
        invoiceItemDao.updateInvoiceItem(invoiceItem);
        assertEquals(99, (int) invoiceItemDao.getInvoiceItem(invoiceItem.getInvoiceItemId()).getQuantity());

    }

    @Test
    public void getAllInvoiceItems() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(10);
        invoice = invoiceDao.addInvoice(invoice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);
        invoiceItem = invoiceItemDao.addInvoiceItem(invoiceItem);
        invoiceItemDao.addInvoiceItem(invoiceItem);

        assertEquals(3, invoiceItemDao.getAllInvoiceItems().size());
    }

    @Test
    public void getInvoiceItemsByInvoiceId() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(10);
        invoice = invoiceDao.addInvoice(invoice);

        Invoice invoice2 = new Invoice();
        invoice2.setCustomerId(10);
        invoice2 = invoiceDao.addInvoice(invoice2);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));
        invoiceItemDao.addInvoiceItem(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceId(invoice2.getInvoiceId());
        invoiceItem2.setInventoryId(5);
        invoiceItem2.setQuantity(2);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));
        invoiceItemDao.addInvoiceItem(invoiceItem2);

        // get by invoice id
        assertEquals(1, invoiceItemDao.getInvoiceItemsByInvoiceId(invoice2.getInvoiceId()).size());

        // get all
        assertEquals(2, invoiceItemDao.getAllInvoiceItems().size());
    }

    @Test
    public void getInvoiceItemWithNullId(){
        assertNull(invoiceItemDao.getInvoiceItem(10000009));
    }

    @Test(expected = NotFoundException.class)
    public void deleteInvoiceItemWithNullId() {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItemDao.deleteInvoiceItem(invoiceItem.getInvoiceItemId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateInvoiceItemWithNullId() {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(10);
        invoice = invoiceDao.addInvoice(invoice);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(invoice.getInvoiceId());
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        // add without binding id
        invoiceItemDao.addInvoiceItem(invoiceItem);

        // update
        invoiceItem.setInvoiceItemId(100202299);
        invoiceItem.setQuantity(99);
        invoiceItemDao.updateInvoiceItem(invoiceItem);
        assertEquals(99, (int) invoiceItemDao.getInvoiceItem(invoiceItem.getInvoiceItemId()).getQuantity());

    }
}