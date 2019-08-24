package com.evanco.retailapiservice.service;

import com.evanco.retailapiservice.model.*;
import com.evanco.retailapiservice.util.feign.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.time.LocalDate;
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

    @Transactional
    public InvoiceViewModelResponse addInvoice(InvoiceViewModel ivm) {
        /*
           This is where we break down and validate InvoiceViewModel input using all available
           clients. Then, return InvoiceViewModelResponse (imv with levelup points).
        */

        InvoiceViewModelResponse ivmRes = new InvoiceViewModelResponse(); // instantiate response object

        setIvmResCustomerId(ivm, ivmRes); // Set customerId

        ivmRes.setPurchaseDate(ivm.getPurchaseDate()); // set purchase date

        setInvoiceItems(ivm, ivmRes); // set invoice items after validation

        ivm = invoiceClient.addInvoice(ivm); // Send ivm to invoice-service via feign after all parts are verified

        ivmRes.setInvoiceId(ivm.getInvoiceId()); // set invoiceId

        setPoints(ivm, ivmRes); // Set levelup info via queue

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
     * Checks for an existing customer,
     *
     * @param ivm    InvoiceViewModel, (users input)
     * @param ivmRes InvoiceViewModelResponse (eventual output)
     */
    private void setIvmResCustomerId(InvoiceViewModel ivm, InvoiceViewModelResponse ivmRes) {
        if (customerClient.getCustomer(ivm.getCustomerId()) == null) {
            throw new IllegalArgumentException("There is no customer matching the given id");
        }
        ivmRes.setCustomerId(ivm.getCustomerId());
    }

    /**
     * Handle invoice item validation
     * @param ivm InvoiceViewModel
     * @param ivmRes InvoiceViewModelResponse
     */
    private void setInvoiceItems(InvoiceViewModel ivm, InvoiceViewModelResponse ivmRes) {
        List<Inventory> inventories = inventoryClient.getAllInventory();
        for (Inventory inventory : inventories) {
            if(productClient.getProduct(inventory.getProductId()) == null){
                throw new IllegalArgumentException("Product " + inventory.getProductId() + " does not exist");
            }
            for (InvoiceItem ii : ivm.getInvoiceItems()) {
                InvoiceItem invoiceItem = new InvoiceItem();

                // check if ii inventory id is valid
                if (ii.getInventoryId() != inventory.getInventoryId()) {
                    throw new IllegalArgumentException("Inventory id " + ii.getInventoryId() + " is not valid");
                }
                invoiceItem.setInventoryId(ii.getInventoryId());

                if(inventory.getQuantity() <= 0){
                    throw new IllegalArgumentException("Product " + inventory.getProductId() + " out of stock");
                }
                if(ii.getQuantity() < inventory.getQuantity()){
                    throw new IllegalArgumentException("Invalid Quantity");
                }
                invoiceItem.setQuantity(inventory.getQuantity());
                invoiceItem.setUnitPrice(ii.getUnitPrice());

            }
        }
        ivmRes.setInvoiceItems(ivm.getInvoiceItems());
    }

    /**
     * Sends info to queue, gets info from circuit breaker
     * @param ivm InvoiceViewModel
     * @param ivmRes InvoiceViewModelResponse
     */
    private void setPoints(InvoiceViewModel ivm, InvoiceViewModelResponse ivmRes) {
        BigDecimal invoiceTotal = new BigDecimal("0");
        for (InvoiceItem ii : ivmRes.getInvoiceItems()) {
            BigDecimal itemTotal = ii.getUnitPrice().multiply(new BigDecimal(ii.getQuantity()));
            invoiceTotal = invoiceTotal.add(itemTotal);
        }
        int pointsToAdd =
                invoiceTotal.divide(new BigDecimal("50")
                        .setScale(0, RoundingMode.FLOOR), RoundingMode.FLOOR)
                        .intValueExact();

        /*
        TODO
        Use circuit breaker to get existing level up values. If customer exists already in levelup,
        send new level up with levelup id attached. Handle create/update in the queue consumer
        */
        if(getLevelUpInfo(ivm.getCustomerId()) == null) {
            LevelUp levelUp = new LevelUp(ivmRes.getCustomerId(), pointsToAdd, LocalDate.now());
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
            System.out.println("LevelUp sent to queue: " + levelUp);
        }else{
            LevelUp levelUp = new LevelUp(
                    getLevelUpInfo(ivm.getCustomerId()).getLevelUpId(),
                    ivmRes.getCustomerId(),
                    pointsToAdd,
                    LocalDate.now());
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, levelUp);
            System.out.println("LevelUp sent to queue: " + levelUp);
        }

    }

    /**
     * Circuit breaker
     * @param customerId int
     * @return LevelUp object
     */
    @HystrixCommand(fallbackMethod = "reliable")
    public LevelUp getLevelUpInfo(int customerId) {
        // use circuit breaker
        URI uri = URI.create("http://localhost:7001/levelups/" + customerId);
        return this.restTemplate.getForObject(uri, LevelUp.class);
    }

    /**Fallback method for circuit breaker*/
    public Integer reliable(int customerId){
        return levelUpClient.getLevelUpPointsByCustomerId(customerId);
    }


}
