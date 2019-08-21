package com.company.adminapiservice.service;

import com.company.adminapiservice.util.messages.Inventory;
import com.company.adminapiservice.util.feign.InventoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InventoryService {

    InventoryClient inventoryClient;

    //constructors

    public InventoryService() {
    }

    @Autowired
    public InventoryService(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    // methods

    public Inventory addInventory(Inventory inventory) {
        return inventoryClient.addInventory(inventory);
    }

    public Inventory getInvenotry(int inventoryId) {
        return inventoryClient.getInventory(inventoryId);
    }

    public void updateInventory(int id, Inventory inventory) {
        inventoryClient.updateInventory(id, inventory);
    }

    public void deleteInventory(int inventoryId) {
        inventoryClient.deleteInventory(inventoryId);
    }

    public List<Inventory> getAllInventory() {
        return inventoryClient.getAllInventory();
    }

}