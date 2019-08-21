package com.company.customerservice.dao;

import com.company.customerservice.model.Customer;

import java.util.List;

public interface CustomerDao {

    // standard CRUD

    Customer addCustomer(Customer customer);

    Customer getCustomer(int id);

    void updateCustomer(Customer customer);

    void deleteCustomer(int id);

    List<Customer> getAllCustomers();

}
