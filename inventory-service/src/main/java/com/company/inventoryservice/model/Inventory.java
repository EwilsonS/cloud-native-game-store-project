package com.company.inventoryservice.model;

import javax.validation.constraints.*;
import java.util.Objects;

public class Inventory {

    private int inventoryId;

    // wrapper and not primitive int to validate if supplied
    @NotNull(message = "Please supply a product id.")
    @Min(value = 0, message = "Product id must be at least {value}.")
    private Integer productId;

    // wrapper and not primitive int to validate if supplied
    @NotNull(message = "Please supply a quantity.")
    @Min(value = 0, message = "Quantity must be at least {value}.")
    private Integer quantity;

    // constructors

    public Inventory() {
    }

    public Inventory(int inventoryId, Integer productId, Integer quantity) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // getters and setters

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // override methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return getInventoryId() == inventory.getInventoryId() &&
                getProductId().equals(inventory.getProductId()) &&
                getQuantity().equals(inventory.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInventoryId(), getProductId(), getQuantity());
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }

}


