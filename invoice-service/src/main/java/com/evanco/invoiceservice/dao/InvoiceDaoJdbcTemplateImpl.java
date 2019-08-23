package com.evanco.invoiceservice.dao;

import com.evanco.invoiceservice.exception.NotFoundException;
import com.evanco.invoiceservice.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class InvoiceDaoJdbcTemplateImpl implements InvoiceDao {

    // init jdbc
    private JdbcTemplate jdbc;

    // prepared statements
    private static final String INSERT_INVOICE = "insert into invoice (customer_id, purchase_date) values(?,?)";
    private static final String SELECT_INVOICE = "select * from invoice where invoice_id=?";
    private static final String SELECT_ALL_INVOICES = "select * from invoice";
    private static final String SELECT_INVOICES_BY_CUSTOMER_ID = "select * from invoice where customer_id=?";
    private static final String UPDATE_INVOICE = "update invoice set customer_id=?, purchase_date=? where invoice_id=?";
    private static final String DELETE_INVOICE = "delete from invoice where invoice_id=?";

    // constructor injection
    @Autowired
    public InvoiceDaoJdbcTemplateImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // implementation
    @Override
    @Transactional
    public Invoice addInvoice(Invoice invoice) {
        invoice.setPurchaseDate(LocalDate.now());
        jdbc.update(
                INSERT_INVOICE,
                invoice.getCustomerId(),
                invoice.getPurchaseDate()
        );
        invoice.setInvoiceId(jdbc.queryForObject("select LAST_INSERT_ID()", Integer.class));
        return invoice;
    }

    @Override
    public Invoice getInvoice(int id) {
        try {
            return jdbc.queryForObject(SELECT_INVOICE, this::mapRowToInvoice, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateInvoice(Invoice invoice) {
        if(getInvoice(invoice.getInvoiceId())== null){
            throw new IllegalArgumentException("Invoice not found");
        }
        jdbc.update(
                UPDATE_INVOICE,
                invoice.getCustomerId(),
                invoice.getPurchaseDate(),
                invoice.getInvoiceId()
        );
    }

    @Override
    @Transactional
    public void deleteInvoice(int id) {
        if (getInvoice(id) == null) {
            throw new NotFoundException("Invoice not found");
        }
        jdbc.update(DELETE_INVOICE, id);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        // will return an empty list, not null, if there are no invoices
        return jdbc.query(SELECT_ALL_INVOICES, this::mapRowToInvoice);

    }

    @Override
    public List<Invoice> getInvoicesByCustomerId(int customerId) {
        // will return an empty list, not null, if there are no invoices
        return jdbc.query(SELECT_INVOICES_BY_CUSTOMER_ID, this::mapRowToInvoice, customerId);

    }


    // mapper
    private Invoice mapRowToInvoice(ResultSet rs, int rowNum) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));
        invoice.setCustomerId(rs.getInt("customer_id"));
        invoice.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
        return invoice;
    }
}
