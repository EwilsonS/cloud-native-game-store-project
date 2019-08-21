package com.company.inventoryservice.dao;

import com.company.inventoryservice.exception.NotFoundException;
import com.company.inventoryservice.model.Inventory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class InventoryDaoJdbcTemplateImplTest {

    @Autowired
    InventoryDao inventoryDao;

    // clear inventory table in database
    @Before
    public void setUp() throws Exception {
        List<Inventory> inventoryList = inventoryDao.getAllInventory();
        for (Inventory i : inventoryList) {
            inventoryDao.deleteInventory(i.getInventoryId());
        }
    }

    // tests addInventory(), getInventory() and deleteInventory()
    @Test
    public void addGetDeleteInventory() {

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        inventory = inventoryDao.addInventory(inventory);

        Inventory inventory1 = inventoryDao.getInventory(inventory.getInventoryId());
        assertEquals(inventory, inventory1);

        inventoryDao.deleteInventory(inventory.getInventoryId());
        inventory1 = inventoryDao.getInventory(inventory.getInventoryId());
        assertNull(inventory1);
    }

    // tests if will return null if try to get inventory with non-existent id
    @Test
    public void getInventoryWithNonExistentId() {
        Inventory inventory = inventoryDao.getInventory(500);
        assertNull(inventory);
    }

    // tests if will throw exception if id provided does not exist when trying to delete inventory
    @Test(expected  = NotFoundException.class)
    public void deleteInventoryWithNonExistentId() {

        inventoryDao.deleteInventory(500);
    }

    // tests updateInventory()
    @Test
    public void updateInventory() {

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        inventory = inventoryDao.addInventory(inventory);

        inventory.setQuantity(20);

        inventoryDao.updateInventory(inventory);

        Inventory inventory1 = inventoryDao.getInventory(inventory.getInventoryId());
        assertEquals(inventory, inventory1);
    }

    // tests if will throw exception if id provided does not exist when trying to update inventory
    @Test(expected  = IllegalArgumentException.class)
    public void updateInventoryWithIllegalArgumentException() {

        Inventory inventory = new Inventory();
        inventory.setInventoryId(500);
        inventory.setProductId(1);
        inventory.setQuantity(10);

        inventoryDao.updateInventory(inventory);

    }

    // tests getAllInventory()
    @Test
    public void getAllInventory() {

        Inventory inventory = new Inventory();
        inventory.setProductId(1);
        inventory.setQuantity(10);

        inventoryDao.addInventory(inventory);

        inventory = new Inventory();
        inventory.setProductId(2);
        inventory.setQuantity(30);

        inventoryDao.addInventory(inventory);

        List<Inventory> iList = inventoryDao.getAllInventory();
        assertEquals(2, iList.size());
    }

}
