package com.evanco.invoiceservice.service;

import com.evanco.invoiceservice.dao.InvoiceDao;
import com.evanco.invoiceservice.dao.InvoiceDaoJdbcTemplateImpl;
import com.evanco.invoiceservice.dao.InvoiceItemDao;
import com.evanco.invoiceservice.dao.InvoiceItemDaoJdbcTemplateImpl;
import com.evanco.invoiceservice.model.Invoice;
import com.evanco.invoiceservice.model.InvoiceItem;
import com.evanco.invoiceservice.model.InvoiceViewModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    InvoiceService invoiceService;
    InvoiceDao invoiceDao;
    InvoiceItemDao invoiceItemDao;

    @Before
    public void setUp() throws Exception {

        // configure mock objects
        setUpInvoiceMock();
        setUpInvoiceItemMock();

        // Passes mock objects
        invoiceService = new InvoiceService(invoiceDao, invoiceItemDao);

    }

    // tests addInvoice()
    @Test
    public void addGetInvoice() {

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        invoiceVM = invoiceService.addInvoice(invoiceVM);

        InvoiceViewModel invoiceVM1 = invoiceService.getInvoice(invoiceVM.getInvoiceId());

        assertEquals(invoiceVM, invoiceVM1);
    }

    // tests if returns null when trying to retrieve invoice with non existent invoice id
    @Test
    public void getInvoiceWithNonExistentId() {
        InvoiceViewModel invoiceVM = invoiceService.getInvoice(500);
        assertNull(invoiceVM);
    }

    // tests if adds empty list when trying to add invoice view model without a list of items
    @Test
    public void addInvoiceVMWithNoList() {

        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setCustomerId(2);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));

        invoiceVM = invoiceService.addInvoice(invoiceVM);

        InvoiceViewModel invoiceVM1 = invoiceService.getInvoice(invoiceVM.getInvoiceId());

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

        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        invoiceService.addInvoice(invoiceVM);

        invoiceVM = new InvoiceViewModel();
        invoiceVM.setCustomerId(2);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));

        invoiceService.addInvoice(invoiceVM);

        List<InvoiceViewModel> fromService = invoiceService.getAllInvoices();

        assertEquals(2, fromService.size());

    }

    // tests deleteInvoice()
    @Test
    public void deleteInvoice() {
        InvoiceViewModel levelUp = invoiceService.getInvoice(1);
        invoiceService.deleteInvoice(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(invoiceDao).deleteInvoice(postCaptor.capture());
        assertEquals(levelUp.getInvoiceId(), postCaptor.getValue().intValue());
    }

    // tests updateInvoice()
    @Test
    public void updateInvoice() {

        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setInvoiceId(2);
        invoiceVM.setCustomerId(3);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM.setInvoiceItems(new ArrayList<>());

        invoiceService.updateInvoice(invoiceVM, invoiceVM.getInvoiceId());
        ArgumentCaptor<Invoice> postCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceDao).updateInvoice(postCaptor.capture());
        assertEquals(invoiceVM.getCustomerId(), postCaptor.getValue().getCustomerId());

    }

    // tests if updates invoice items updateInvoice()
    @Test
    public void updateInvoiceItems() {

        InvoiceViewModel invoiceVM = new InvoiceViewModel();
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
        verify(invoiceDao).updateInvoice(postCaptor.capture());
        assertEquals(invoiceVM.getCustomerId(), postCaptor.getValue().getCustomerId());

        verify(invoiceItemDao).getInvoiceItemsByInvoiceId(postCaptor.getValue().getInvoiceId());

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

        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setInvoiceId(1);
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        InvoiceViewModel invoiceVM2 = new InvoiceViewModel();
        invoiceVM2.setInvoiceId(2);
        invoiceVM2.setCustomerId(2);
        invoiceVM2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM2.setInvoiceItems(new ArrayList<>());

        List<InvoiceViewModel> invoicesCust1 = new ArrayList<>();
        invoicesCust1.add(invoiceVM);

        List<InvoiceViewModel> invoicesCust2 = new ArrayList<>();
        invoicesCust2.add(invoiceVM2);

        List<InvoiceViewModel> invoicesForCust1FromService= invoiceService.getInvoicesByCustomerId(1);

        assertEquals(invoicesCust1, invoicesForCust1FromService);

        List<InvoiceViewModel> invoicesForCust2FromService= invoiceService.getInvoicesByCustomerId(2);

        assertEquals(invoicesCust2, invoicesForCust2FromService);
    }

    // Create mocks

    public void setUpInvoiceMock() {
        invoiceDao = mock(InvoiceDaoJdbcTemplateImpl.class);

        Invoice invoice = new Invoice();
        invoice.setCustomerId(1);
        invoice.setPurchaseDate(LocalDate.of(2019, 7, 15));

        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceId(1);
        invoice2.setCustomerId(1);
        invoice2.setPurchaseDate(LocalDate.of(2019, 7, 15));

        Invoice invoice3 = new Invoice();
        invoice3.setCustomerId(2);
        invoice3.setPurchaseDate(LocalDate.of(2019, 5, 10));

        Invoice invoice4 = new Invoice();
        invoice4.setInvoiceId(2);
        invoice4.setCustomerId(2);
        invoice4.setPurchaseDate(LocalDate.of(2019, 5, 10));

        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoice2);
        invoiceList.add(invoice4);

        List<Invoice> invoiceListCust1 = new ArrayList<>();
        invoiceListCust1.add(invoice2);

        List<Invoice> invoiceListCust2 = new ArrayList<>();
        invoiceListCust2.add(invoice4);

        doReturn(invoice2).when(invoiceDao).addInvoice(invoice);
        doReturn(invoice4).when(invoiceDao).addInvoice(invoice3);

        doReturn(invoice2).when(invoiceDao).getInvoice(1);
        doReturn(invoice4).when(invoiceDao).getInvoice(2);

        doReturn(invoiceList).when(invoiceDao).getAllInvoices();

        doReturn(invoiceListCust1).when(invoiceDao).getInvoicesByCustomerId(1);
        doReturn(invoiceListCust2).when(invoiceDao).getInvoicesByCustomerId(2);
    }


    public void setUpInvoiceItemMock() {
        invoiceItemDao = mock(InvoiceItemDaoJdbcTemplateImpl.class);

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceId(1);
        invoiceItem.setInventoryId(5);
        invoiceItem.setQuantity(2);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(1);
        invoiceItem2.setInvoiceId(1);
        invoiceItem2.setInventoryId(5);
        invoiceItem2.setQuantity(2);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        List<InvoiceItem> invoiceItemsOnInvoice1 = new ArrayList<>();
        invoiceItemsOnInvoice1.add(invoiceItem2);

        doReturn(invoiceItem2).when(invoiceItemDao).addInvoiceItem(invoiceItem);

        doReturn(invoiceItem2).when(invoiceItemDao).getInvoiceItem(1);

        doReturn(invoiceItemsOnInvoice1).when(invoiceItemDao).getInvoiceItemsByInvoiceId(1);

    }

}
