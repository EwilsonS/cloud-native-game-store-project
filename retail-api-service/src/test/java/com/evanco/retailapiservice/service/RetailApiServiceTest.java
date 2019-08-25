package com.evanco.retailapiservice.service;

import com.evanco.retailapiservice.model.*;
import com.evanco.retailapiservice.util.feign.InventoryClient;
import com.evanco.retailapiservice.util.feign.InvoiceClient;
import com.evanco.retailapiservice.util.feign.ProductClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RetailApiServiceTest {


    @InjectMocks
    private RetailApiService service;

    @Mock
    private ProductClient productClient;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private InvoiceClient invoiceClient;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // configure mock objects
        setUpProductClientMock();
        setUpInventoryClientMock();
        setUpInvoiceClientMock();

    }

    // TESTS PRODUCT ROUTES

    // tests getProduct()
    @Test
    public void getProduct() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product1 = service.getProduct(product.getProductId());

        assertEquals(product, product1);
    }

    // tests getProductsInInventory()
    @Test
    public void getProductsInInventory() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("The Legend of Zelda: Link's Awakening");
        product2.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product2.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product2.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        List<Product> productsFromService = service.getProductsInInventory();

        assertEquals(productList, productsFromService);

    }

    // tests getProductsByInvoiceId()
    @Test
    public void getProductsByInvoiceId() {

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setProductId(2);
        product2.setProductName("The Legend of Zelda: Link's Awakening");
        product2.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product2.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product2.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        List<Product> productsFromService = service.getProductsByInvoiceId(114);

        assertEquals(productList, productsFromService);

    }

    // TESTS INVOICE ROUTES

    // tests getInvoice()
    @Test
    public void getInvoice() {

        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setInvoiceId(2);
        invoiceVM.setCustomerId(2);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM.setInvoiceItems(new ArrayList<>());

        InvoiceViewModel invoiceVM1 = service.getInvoice(invoiceVM.getInvoiceId());

        assertEquals(invoiceVM, invoiceVM1);

    }

    // tests getAllInvoices()
    @Test
    public void getAllInvoices() {

        // invoice items
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(114);
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(1);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(2);
        invoiceItem2.setInvoiceId(114);
        invoiceItem2.setInventoryId(2);
        invoiceItem2.setQuantity(5);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem2);

        // invoice
        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setInvoiceId(114);
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        // invoice 2
        InvoiceViewModel invoiceVM2 = new InvoiceViewModel();
        invoiceVM2.setInvoiceId(2);
        invoiceVM2.setCustomerId(2);
        invoiceVM2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM2.setInvoiceItems(new ArrayList<>());

        List<InvoiceViewModel> invoiceList = new ArrayList<>();
        invoiceList.add(invoiceVM);
        invoiceList.add(invoiceVM2);

        List<InvoiceViewModel> fromService = service.getAllInvoices();

        assertEquals(2, fromService.size());
        assertEquals(invoiceList, fromService);

    }

    // tests getInvoicesByCustomerId()
    @Test
    public void getInvoicesByCustomerId() {

        // invoice items
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(114);
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(1);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(2);
        invoiceItem2.setInvoiceId(114);
        invoiceItem2.setInventoryId(2);
        invoiceItem2.setQuantity(5);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem2);

        // invoice
        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setInvoiceId(114);
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        // invoice 2
        InvoiceViewModel invoiceVM2 = new InvoiceViewModel();
        invoiceVM2.setInvoiceId(2);
        invoiceVM2.setCustomerId(2);
        invoiceVM2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM2.setInvoiceItems(new ArrayList<>());

        List<InvoiceViewModel> invoicesCust1 = new ArrayList<>();
        invoicesCust1.add(invoiceVM);

        List<InvoiceViewModel> invoicesCust2 = new ArrayList<>();
        invoicesCust2.add(invoiceVM2);

        List<InvoiceViewModel> invoicesForCust1FromService = service.getInvoicesByCustomerId(1);

        assertEquals(invoicesCust1, invoicesForCust1FromService);

        List<InvoiceViewModel> invoicesForCust2FromService = service.getInvoicesByCustomerId(2);

        assertEquals(invoicesCust2, invoicesForCust2FromService);
    }

    // Create mocks

    public void setUpProductClientMock() {

        Product product = new Product();
        product.setProductName("Mario Kart 8 Deluxe");
        product.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product2 = new Product();
        product2.setProductId(1);
        product2.setProductName("Mario Kart 8 Deluxe");
        product2.setProductDescription("Play anytime, anywhere! Race your friends or battle them.");
        product2.setListPrice(new BigDecimal(49.99).setScale(2, RoundingMode.HALF_UP));
        product2.setUnitCost(new BigDecimal(20.00).setScale(2, RoundingMode.HALF_UP));

        Product product3 = new Product();
        product3.setProductName("The Legend of Zelda: Link's Awakening");
        product3.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product3.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product3.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        Product product4 = new Product();
        product4.setProductId(2);
        product4.setProductName("The Legend of Zelda: Link's Awakening");
        product4.setProductDescription("As Link, explore a reimagined Koholint Island and collect instruments to awaken the Wind Fish to find a way home.");
        product4.setListPrice(new BigDecimal(59.99).setScale(2, RoundingMode.HALF_UP));
        product4.setUnitCost(new BigDecimal(39.00).setScale(2, RoundingMode.HALF_UP));

        doReturn(product2).when(productClient).getProduct(1);
        doReturn(product4).when(productClient).getProduct(2);

    }

    public void setUpInventoryClientMock() {

        Inventory inventory = new Inventory();
        inventory.setInventoryId(1);
        inventory.setProductId(1);
        inventory.setQuantity(10);

        Inventory inventory2 = new Inventory();
        inventory2.setInventoryId(2);
        inventory2.setProductId(2);
        inventory2.setQuantity(30);

        List<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(inventory);
        inventoryList.add(inventory2);

        doReturn(inventoryList).when(inventoryClient).getAllInventory();
        doReturn(inventory).when(inventoryClient).getInventory(inventory.getInventoryId());
        doReturn(inventory2).when(inventoryClient).getInventory(inventory2.getInventoryId());

    }

    public void setUpInvoiceClientMock() {

        // invoice items
        List<InvoiceItem> invoiceItems = new ArrayList<>();

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoiceItemId(1);
        invoiceItem.setInvoiceId(114);
        invoiceItem.setInventoryId(1);
        invoiceItem.setQuantity(1);
        invoiceItem.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem);

        InvoiceItem invoiceItem2 = new InvoiceItem();
        invoiceItem2.setInvoiceItemId(2);
        invoiceItem2.setInvoiceId(114);
        invoiceItem2.setInventoryId(2);
        invoiceItem2.setQuantity(5);
        invoiceItem2.setUnitPrice(new BigDecimal("29.99"));

        invoiceItems.add(invoiceItem2);

        // invoice
        InvoiceViewModel invoiceVM = new InvoiceViewModel();
        invoiceVM.setInvoiceId(114);
        invoiceVM.setCustomerId(1);
        invoiceVM.setPurchaseDate(LocalDate.of(2019, 7, 15));
        invoiceVM.setInvoiceItems(invoiceItems);

        // invoice 2
        InvoiceViewModel invoiceVM2 = new InvoiceViewModel();
        invoiceVM2.setInvoiceId(2);
        invoiceVM2.setCustomerId(2);
        invoiceVM2.setPurchaseDate(LocalDate.of(2019, 5, 10));
        invoiceVM2.setInvoiceItems(new ArrayList<>());

        List<InvoiceViewModel> invoiceList = new ArrayList<>();
        invoiceList.add(invoiceVM);
        invoiceList.add(invoiceVM2);

        doReturn(invoiceVM).when(invoiceClient).getInvoice(invoiceVM.getInvoiceId());
        doReturn(invoiceVM2).when(invoiceClient).getInvoice(2);
        doReturn(invoiceList).when(invoiceClient).getAllInvoices();

        List<InvoiceViewModel> invoiceListCust1 = new ArrayList<>();
        invoiceItem.setInvoiceItemId(1);
        invoiceListCust1.add(invoiceVM);

        List<InvoiceViewModel> invoiceListCust2 = new ArrayList<>();
        invoiceListCust2.add(invoiceVM2);

        doReturn(invoiceListCust1).when(invoiceClient).getInvoicesByCustomerId(1);
        doReturn(invoiceListCust2).when(invoiceClient).getInvoicesByCustomerId(2);

    }

}