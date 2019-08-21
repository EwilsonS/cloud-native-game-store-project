package com.company.inventoryservice.dao;

import com.company.inventoryservice.exception.NotFoundException;
import com.company.inventoryservice.model.Inventory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InventoryDaoJdbcTemplateImpl implements InventoryDao {

    private JdbcTemplate jdbcTemplate;

    // prepared statements

    private static final String INSERT_INVENTORY_SQL =
            "insert into inventory (product_id, quantity) values (?, ?)";

    private static final String SELECT_INVENTORY_SQL =
            "select * from inventory where inventory_id = ?";

    private static final String UPDATE_INVENTORY_SQL =
            "update inventory set product_id = ?, quantity = ? where inventory_id = ?";

    private static final String DELETE_INVENTORY_SQL =
            "delete from inventory where inventory_id = ?";

    private static final String SELECT_ALL_INVENTORY_SQL =
            "select * from inventory";

    // constructor

    public InventoryDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // implement methods

    @Override
    @Transactional
    public Inventory addInventory(Inventory inventory) {
        jdbcTemplate.update(
                INSERT_INVENTORY_SQL,
                inventory.getProductId(),
                inventory.getQuantity()
        );

        int id = jdbcTemplate.queryForObject("select LAST_INSERT_ID()", Integer.class);

        inventory.setInventoryId(id);

        return inventory;
    }

    @Override
    public Inventory getInventory(int inventoryId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_INVENTORY_SQL, this::mapRowToInventory, inventoryId);
        } catch (EmptyResultDataAccessException e) {
            // if there is no match for this id, return null
            return null;
        }
    }

    @Override
    @Transactional
    public void updateInventory(Inventory inventory) {
        // checks for id first so user knows if anything was updated
        // user could have unknowingly entered the wrong id
        Inventory inventoryInDB = getInventory(inventory.getInventoryId());
        if (inventoryInDB == null) {
            throw new IllegalArgumentException("The id provided does not exist.");
        }

        jdbcTemplate.update(
                UPDATE_INVENTORY_SQL,
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getInventoryId()
        );
    }

    @Override
    @Transactional
    public void deleteInventory(int inventoryId) {
        // checks for id first so user knows if anything was deleted
        // user could have unknowingly entered the wrong id
        Inventory inventoryInDB = getInventory(inventoryId);
        if (inventoryInDB == null) {
            throw new NotFoundException("The id provided does not exist.");
        }

        jdbcTemplate.update(DELETE_INVENTORY_SQL, inventoryId);
    }

    @Override
    public List<Inventory> getAllInventory() {
        return jdbcTemplate.query(SELECT_ALL_INVENTORY_SQL, this::mapRowToInventory);
    }

    // Row Mapper

    private Inventory mapRowToInventory(ResultSet rs, int rowNum) throws SQLException {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(rs.getInt("inventory_id"));
        inventory.setProductId(rs.getInt("product_id"));
        inventory.setQuantity(rs.getInt("quantity"));

        return inventory;
    }

}
