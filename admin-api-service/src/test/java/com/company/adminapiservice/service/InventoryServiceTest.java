package com.company.adminapiservice.service;

import com.company.adminapiservice.util.feign.InventoryClient;
import com.company.adminapiservice.util.messages.Inventory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryClient inventoryClient;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // configure mock objects
        setUpInventoryClientMock();

    }

    // tests addInventory()
    @Test
    public void addInventory() {
        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        inventory = inventoryService.addInventory(inventory);

        Inventory inventory1 = inventoryService.getInvenotry(inventory.getInventoryId());

        assertEquals(inventory, inventory1);
    }

    // tests getInventory()
    @Test
    public void getInventory() {

        Inventory inventory = new Inventory();
        inventory.setInventoryId(1);
        inventory.setProductId(1);
        inventory.setQuantity(10);

        Inventory inventory1 = inventoryService.getInvenotry(inventory.getInventoryId());

        assertEquals(inventory, inventory1);
    }

    // tests getAllInventory()
    @Test
    public void getAllInventory() {

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        inventoryService.addInventory(inventory);

        inventory = new Inventory();
        inventory.setProductId(2);
        inventory.setQuantity(30);

        inventoryService.addInventory(inventory);

        List<Inventory> fromService = inventoryService.getAllInventory();

        assertEquals(2, fromService.size());

    }

    // tests deleteInventory()
    @Test
    public void deleteInventory() {
        Inventory inventory = inventoryService.getInvenotry(1);
        inventoryService.deleteInventory(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(inventoryClient).deleteInventory(postCaptor.capture());
        assertEquals(inventory.getInventoryId(), postCaptor.getValue().intValue());
    }

    // tests updateInventory()
    @Test
    public void updateInventory() {

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(100);

        inventoryService.updateInventory(inventory.getInventoryId(), inventory);
        ArgumentCaptor<Inventory> postCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryClient).updateInventory(any(Integer.class), postCaptor.capture());
        assertEquals(inventory.getQuantity(), postCaptor.getValue().getQuantity());

    }

    // tests if will return null if try to get inventory with non-existent id
    @Test
    public void getInventoryWithNonExistentId() {
        Inventory inventory = inventoryService.getInvenotry(500);
        assertNull(inventory);
    }

    // tests default constructor for test coverage
    // so developers know something went wrong if less than 100%
    @Test
    public void createADefaultInventory() {

        Object inventoryObj = new InventoryService();

        assertEquals(true , inventoryObj instanceof InventoryService);

    }

    // Create mocks

    public void setUpInventoryClientMock() {

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        Inventory inventory2 = new Inventory();
        inventory2.setInventoryId(1);
        inventory2.setProductId(1);
        inventory2.setQuantity(10);

        Inventory inventory3 = new Inventory();
        inventory3.setProductId(2);
        inventory3.setQuantity(30);

        Inventory inventory4 = new Inventory();
        inventory4.setInventoryId(2);
        inventory4.setProductId(2);
        inventory4.setQuantity(30);

        doReturn(inventory2).when(inventoryClient).addInventory(inventory);
        doReturn(inventory4).when(inventoryClient).addInventory(inventory3);
        doReturn(inventory2).when(inventoryClient).getInventory(1);
        doReturn(inventory4).when(inventoryClient).getInventory(2);

        List<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(inventory2);
        inventoryList.add(inventory4);

        doReturn(inventoryList).when(inventoryClient).getAllInventory();

    }

}


