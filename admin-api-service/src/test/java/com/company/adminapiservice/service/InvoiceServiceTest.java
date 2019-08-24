package com.company.adminapiservice.service;

import com.company.adminapiservice.util.feign.InvoiceClient;

import com.company.adminapiservice.util.messages.Invoice;
import com.company.adminapiservice.util.messages.InvoiceItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {


    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private InvoiceClient invoiceClient;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // configure mock objects
        setUpInvoiceClientMock();

    }

    // tests addInvoice()
    @Test
    public void addGetInvoice() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(0);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        Invoice invoiceVM = new Invoice();
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        invoiceVM = invoiceService.addInvoice(invoiceVM);

        Invoice invoiceVM1 = invoiceService.getInvoice(invoiceVM.getInvoiceId());

        assertEquals(invoiceVM, invoiceVM1);
    }

    // tests if returns null when trying to retrieve invoice with non existent invoice id
    @Test
    public void getInvoiceWithNonExistentId() {
        Invoice invoiceVM = invoiceService.getInvoice(500);
        assertNull(invoiceVM);
    }

    // tests if adds empty list when trying to add invoice view model without a list of items
    @Test
    public void addInvoiceVMWithNoList() {

        Invoice invoiceVM = new Invoice();
        invoiceVM.setCustomerId(2);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));

        invoiceVM = invoiceService.addInvoice(invoiceVM);

        Invoice invoiceVM1 = invoiceService.getInvoice(invoiceVM.getInvoiceId());

        assertEquals(invoiceVM, invoiceVM1);
        assertEquals(0, invoiceVM1.getInvoiceItems().size());

    }

    // tests getAllInvoices()
    @Test
    public void getAllInvoices() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        Invoice invoiceVM = new Invoice();
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        invoiceService.addInvoice(invoiceVM);

        invoiceVM = new Invoice();
        invoiceVM.setCustomerId(2);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));

        invoiceService.addInvoice(invoiceVM);

        List<Invoice> fromService = invoiceService.getAllInvoices();

        assertEquals(2, fromService.size());

    }

    // tests deleteInvoice()
    @Test
    public void deleteInvoice() {
        Invoice levelUp = invoiceService.getInvoice(1);
        invoiceService.deleteInvoice(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(invoiceClient).deleteInvoice(postCaptor.capture());
        assertEquals(levelUp.getInvoiceId(), postCaptor.getValue().intValue());
    }

    // tests updateInvoice()
    @Test
    public void updateInvoice() {

        Invoice invoiceVM = new Invoice();
        invoiceVM.setInvoiceId(2);
        invoiceVM.setCustomerId(3);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM.setInvoiceItems(new ArrayList<>());

        invoiceService.updateInvoice(invoiceVM, invoiceVM.getInvoiceId());
        ArgumentCaptor<Invoice> postCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceClient).updateInvoice(postCaptor.capture(), any(Integer.class));
        assertEquals(invoiceVM.getCustomerId(), postCaptor.getValue().getCustomerId());

    }

    // tests if updates invoice items updateInvoice()
    @Test
    public void updateInvoiceItems() {

        Invoice invoiceVM = new Invoice();
        invoiceVM.setInvoiceId(2);
        invoiceVM.setCustomerId(3);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        List<InvoiceItem> itemList = new ArrayList<>();
        itemList.add(invoiceItem);

        invoiceVM.setInvoiceItems(itemList);

        invoiceService.updateInvoice(invoiceVM, invoiceVM.getInvoiceId());
        ArgumentCaptor<Invoice> postCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceClient).updateInvoice(postCaptor.capture(), any(Integer.class));
        assertEquals(invoiceVM.getCustomerId(), postCaptor.getValue().getCustomerId());

    }

    // tests getInvoicesByCustomerId()
    @Test
    public void getInvoicesByCustomerId() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        Invoice invoiceVM = new Invoice();
        invoiceVM.setInvoiceId(1);
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        Invoice invoiceVM2 = new Invoice();
        invoiceVM2.setInvoiceId(2);
        invoiceVM2.setCustomerId(2);
        invoiceVM2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM2.setInvoiceItems(new ArrayList<>());

        List<Invoice> invoicesCust1 = new ArrayList<>();
        invoicesCust1.add(invoiceVM);

        List<Invoice> invoicesCust2 = new ArrayList<>();
        invoicesCust2.add(invoiceVM2);

        List<Invoice> invoicesForCust1FromService = invoiceService.getInvoicesByCustomerId(1);

        assertEquals(invoicesCust1, invoicesForCust1FromService);

        List<Invoice> invoicesForCust2FromService= invoiceService.getInvoicesByCustomerId(2);

        assertEquals(invoicesCust2, invoicesForCust2FromService);
    }

    // tests default constructor for test coverage
    // so developers know something went wrong if less than 100%
    @Test
    public void createADefaultInventory() {

        Object invoiceObj = new InvoiceService();

        assertEquals(true , invoiceObj instanceof InvoiceService);

    }

    // Create mocks

    public void setUpInvoiceClientMock() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(0);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoice.setInvoiceItems(invoiceItems);

        List<InvoiceItem> invoiceItems2 = new ArrayList<>();

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(1);
        invoiceItem2.setInvoiceId(1);
        invoiceItem2.setInventoryId(5);
        invoiceItem2.setQuantity(2);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems2.add(invoiceItem2);

        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceId(1);
        invoice2.setCustomerId(1);
        invoice2.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoice2.setInvoiceItems(invoiceItems2);

        doReturn(invoice2).when(invoiceClient).addInvoice(invoice);
        doReturn(invoice2).when(invoiceClient).getInvoice(1);

        Invoice invoice3 = new Invoice();
        invoice3.setCustomerId(2);
        invoice3.setPurchaseDate(LocalDate.of(2019, 5, 10));

        Invoice invoice4 = new Invoice();
        invoice4.setInvoiceId(2);
        invoice4.setCustomerId(2);
        invoice4.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoice4.setInvoiceItems(new ArrayList<>());

        doReturn(invoice4).when(invoiceClient).addInvoice(invoice3);
        doReturn(invoice4).when(invoiceClient).getInvoice(2);

        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoice2);
        invoiceList.add(invoice4);

        List<Invoice> invoiceListCust1 = new ArrayList<>();
        invoiceItem.setInvoiceItemId(1);
        invoiceListCust1.add(invoice2);

        List<Invoice> invoiceListCust2 = new ArrayList<>();
        invoiceListCust2.add(invoice4);

        doReturn(invoiceList).when(invoiceClient).getAllInvoices();

        doReturn(invoiceListCust1).when(invoiceClient).getInvoiceByCustomerId(1);
        doReturn(invoiceListCust2).when(invoiceClient).getInvoiceByCustomerId(2);
    }

}
