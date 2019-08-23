package com.evanco.invoiceservice.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceItem{

    private int invoiceItemId;

    // wrapper and not primitive int to validate if supplied
//    @NotNull(message = "Please supply an invoice id.")
    @Positive
    private Integer invoiceId;

    // wrapper and not primitive int to validate if supplied
//    @NotNull(message = "BLEHHHHH")
    @Positive
    private Integer inventoryId;

    // wrapper and not primitive int to validate if supplied
//    @NotNull(message = "Please supply a quantity.")
    private Integer quantity;

//    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "The min value you can enter for unit price is {value}.")
    @DecimalMax(value = "99999.99", inclusive = true, message = "The max value you can enter for unit price is {value}")
    private BigDecimal unitPrice;

    // getters and setters

    public int getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(int invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public int getInvoiceId() {
        // added b/c @Valid not working to test @NotNull of list objects
        if (invoiceId == null) {
            throw new NullPointerException("An invoice id is required");
        }
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInventoryId() {
        // added b/c @Valid not working to test @NotNull of list objects
        if (inventoryId == null) {
            throw new NullPointerException("An inventory id is required");
        }
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getQuantity() {
        // added b/c @Valid not working to test @NotNull of list objects
        if (quantity == null) {
            throw new NullPointerException("A quantity is required");
        }
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        // added b/c @Valid not working to test @NotNull of list objects
        if (unitPrice == null) {
            throw new NullPointerException("A unit price is required");
        }
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    // override methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceItem that = (InvoiceItem) o;
        return invoiceItemId == that.invoiceItemId &&
                invoiceId == that.invoiceId &&
                inventoryId == that.inventoryId &&
                quantity == that.quantity &&
                unitPrice.equals(that.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceItemId, invoiceId, inventoryId, quantity, unitPrice);
    }

    @Override
    public String toString() {
        return "InvoiceItem{" +
                "invoiceItemId=" + invoiceItemId +
                ", invoiceId=" + invoiceId +
                ", inventoryId=" + inventoryId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }

}
