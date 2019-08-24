package com.evanco.invoiceservice.model;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class InvoiceViewModel extends Invoice implements Serializable {

    @Valid
    private List<InvoiceItem> invoiceItems;

    // getters and setters

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    // constructors

    public InvoiceViewModel() {

    }

    public InvoiceViewModel(int invoiceId, Integer customerId, LocalDate purchaseDate, List<InvoiceItem> invoiceItems) {
        super(invoiceId, customerId, purchaseDate);
        this.invoiceItems = invoiceItems;
    }

    // override methods

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InvoiceViewModel that = (InvoiceViewModel) o;
        return getInvoiceItems().equals(that.getInvoiceItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getInvoiceItems());
    }

    @Override
    public String toString() {
        return "InvoiceViewModel{" +
                "Invoice{" +
                "invoiceId=" + invoiceId +
                ", customerId=" + customerId +
                ", purchaseDate=" + purchaseDate +
                '}' +
                "invoiceItems=" + invoiceItems +
                '}';
    }
}
