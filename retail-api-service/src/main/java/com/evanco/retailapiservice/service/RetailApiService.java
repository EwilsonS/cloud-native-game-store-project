package com.evanco.retailapiservice.service;

import com.evanco.retailapiservice.exception.InsufficientQuantityException;
import com.evanco.retailapiservice.exception.NotFoundException;
import com.evanco.retailapiservice.model.*;
import com.evanco.retailapiservice.util.feign.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class RetailApiService {

    private static final String EXCHANGE = "levelup-exchange";
    private static final String ROUTING_KEY = "levelup.create.retail.service";

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private InventoryClient inventoryClient;
    @Autowired
    private LevelUpClient levelUpClient;
    @Autowired
    private InvoiceClient invoiceClient;
    @Autowired
    private ProductClient productClient;

    private final RestTemplate restTemplate;

    public RetailApiService(
            RabbitTemplate rabbitTemplate, CustomerClient customerClient,
            InventoryClient inventoryClient, LevelUpClient levelUpClient,
            InvoiceClient invoiceClient, ProductClient productClient, RestTemplate restTemplate) {
        this.customerClient = customerClient;
        this.inventoryClient = inventoryClient;
        this.invoiceClient = invoiceClient;
        this.levelUpClient = levelUpClient;
        this.productClient = productClient;
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
    }

    //---------------------------------------------------
    // Service Methods
    //---------------------------------------------------

    // SUBMIT INVOICE METHODS

    @Transactional
    public InvoiceViewModelResponse addInvoice(InvoiceViewModel ivm) {
        /*
           This is where we break down and validate InvoiceViewModel input using all available
           clients. Then, return InvoiceViewModelResponse (imv with levelup points).
        */

        // throws exception if customer does not exist
        checkIfCustomer(ivm);

        // throws exception if contains invalid invoice items
        validateInvoiceItems(ivm);

        // populates response with request info
        InvoiceViewModelResponse ivmRes = new InvoiceViewModelResponse();
        ivmRes.setCustomerId(ivm.getCustomerId());
        ivmRes.setInvoiceItems(ivm.getInvoiceItems());

        // add invoice to db
        InvoiceViewModel invoiceVM = invoiceClient.addInvoice(ivm);

        // add invoice id and purchase date to response
        ivmRes.setInvoiceId(invoiceVM.getInvoiceId());
        ivmRes.setPurchaseDate(invoiceVM.getPurchaseDate());

        // add invoice id and invoice item id to invoice items
        List<InvoiceItem> invoiceItems = ivmRes.getInvoiceItems();
        for (int i = 0; i < ivmRes.getInvoiceItems().size(); i++) {
            invoiceItems.get(i).setInvoiceId(invoiceVM.getInvoiceId());
            invoiceItems.get(i).setInvoiceItemId(invoiceVM.getInvoiceItems().get(i).getInvoiceItemId());
        }

        // get points
        Integer points = calculatePoints(ivm);

        // set points
        ivmRes.setPoints(points);

        return ivmRes;
    }

    // get all invoices
    // get invoice
    // get invoice by customer id
    // get all products in inventory
    // get product
    // ger products by invoice id
    // get levelup points by customer id

    //---------------------------------------------------
    // Helpers
    //---------------------------------------------------

    /**
     * Checks for an existing customer
     * @param ivm InvoiceViewModel
     */
    public void checkIfCustomer(InvoiceViewModel ivm) {
        if (customerClient.getCustomer(ivm.getCustomerId()) == null) {
            throw new IllegalArgumentException("There is no customer matching the given id");
        }
    }

    /**
     * Handle invoice item validation
     * @param ivm InvoiceViewModel
     */
    public void validateInvoiceItems(InvoiceViewModel ivm) {

        ivm.getInvoiceItems().forEach(ii -> {

            Inventory inventory = inventoryClient.getInventory(ii.getInventoryId());

            // throw exception if the inventory id does not exist
            if (inventory == null) {
                throw new IllegalArgumentException("Inventory id " + ii.getInventoryId() + " is not valid");
            }

            int quantityInStock = inventory.getQuantity();

            // throw exception if quantity requested not available
            if (ii.getQuantity() > quantityInStock || ii.getQuantity() < 0) {
                throw new InsufficientQuantityException("We do not have that quantity.");
            }

            // throw exception if item does not exist
            if (productClient.getProduct(inventory.getProductId()) == null) {
                throw new IllegalArgumentException("Product " + inventory.getProductId() + " does not exist");
            }

        });

    }

    /**
     * Sends info to queue, gets info from circuit breaker
     * @param ivm
     * @return
     */
    public Integer calculatePoints(InvoiceViewModel ivm) {

        // get total purchase value
        BigDecimal invoiceTotal = new BigDecimal("0");
        for (InvoiceItem ii : ivm.getInvoiceItems()) {
            BigDecimal itemTotal = ii.getUnitPrice().multiply(new BigDecimal(ii.getQuantity()));
            invoiceTotal = invoiceTotal.add(itemTotal);
        }

        // gets points earned
        Integer pointsEarned =  invoiceTotal.divide(new BigDecimal("50")
                .setScale(0, BigDecimal.ROUND_FLOOR), BigDecimal.ROUND_FLOOR).intValue();

        // get previous points
        Integer previousPoints = getPoints(ivm.getCustomerId());

        int totalPoints = pointsEarned;

        if (previousPoints != null) {
            totalPoints += previousPoints;
        }

        // Sent via Queue
        // if customer not already a member of level up, add them as a member with their earned points
        if (previousPoints == null) {
            LevelUp levelUp = new LevelUp(ivm.getCustomerId(), pointsEarned, LocalDate.now());
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
        }
        // if customer already a member of level up, add points to level up in db
        else {
            LevelUp levelUp = new LevelUp(
                    levelUpClient.getLevelUpByCustomerId(ivm.getCustomerId()).getLevelUpId(),
                    ivm.getCustomerId(),
                    totalPoints,
                    LocalDate.now());
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
        }

        return  totalPoints;

    }

    // LEVEL UP METHODS

    @HystrixCommand(fallbackMethod = "getPointsFallback")
    public Integer getPoints(int customerId) {
        if (levelUpClient.getLevelUpByCustomerId(customerId) == null) {
            return null;
        } else {
            return levelUpClient.getLevelUpByCustomerId(customerId).getPoints();
        }
    }

    /**Fallback method for circuit breaker*/
    public Integer getPointsFallback(int customerId){
        throw new NotFoundException("LevelUp could not be retrieved for customer id " + customerId);
    }

    // INVOICE GET METHODS

    public InvoiceViewModel getInvoice(int id) {
        return invoiceClient.getInvoice(id);
    }

    public List<InvoiceViewModel> getAllInvoices() {
        List<InvoiceViewModel> invoices = invoiceClient.getAllInvoices();
        return invoiceClient.getAllInvoices();
    }

    public List<InvoiceViewModel> getInvoicesByCustomerId(int id) {
        return invoiceClient.getInvoicesByCustomerId(id);
    }

    // PRODUCT GET METHODS

    public Product getProduct(int id) {
        return productClient.getProduct(id);
    }

    public List<Product> getProductsInInventory() {

        List<Product> products = new ArrayList<>();

        // get all inventory
        List<Inventory> inventory = inventoryClient.getAllInventory();

        // loop through inventory list to get product id
        // use product id to get product to add to product list
        inventory.forEach(ii ->
            products.add(productClient.getProduct(ii.getProductId())));

        return products;

    }

    public List<Product> getProductsByInvoiceId(int id) {

        List<Product> products = new ArrayList<>();

        // get invoice by invoice id
        InvoiceViewModel invoice = invoiceClient.getInvoice(id);

        // loop through invoice items in invoice to get inventory ids of products
        invoice.getInvoiceItems().forEach(ii -> {

            // use inventory id to get inventory which has product id
            Inventory inventory = inventoryClient.getInventory(ii.getInventoryId());

            // use product id to get product to add to product list
            products.add(productClient.getProduct(inventory.getProductId()));

        });

        return products;

    }

}
