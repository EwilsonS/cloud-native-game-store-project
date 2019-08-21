package com.company.inventoryservice.dao;

import com.company.inventoryservice.model.Inventory;

import java.util.List;

public interface InventoryDao {

    // standard CRUD

    Inventory addInventory(Inventory inventory);

    Inventory getInventory(int inventoryId);

    void updateInventory(Inventory inventory);

    void deleteInventory(int inventoryId);

    List<Inventory> getAllInventory();

}
