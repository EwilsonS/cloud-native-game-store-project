package com.evanco.retailapiservice.util.feign;

import com.evanco.retailapiservice.model.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    // get inventory
    @GetMapping(value = "/inventory")
    List<Inventory> getAllInventory();

    @GetMapping(value = "/inventory/{id}")
    public Inventory getInventory(@PathVariable int id);

}
