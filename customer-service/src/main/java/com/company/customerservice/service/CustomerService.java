package com.company.customerservice.service;

import com.company.customerservice.dao.CustomerDao;
import com.company.customerservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerService {

    CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Customer addCustomer(Customer customer) {
        return customerDao.addCustomer(customer);
    }

    public Customer getCustomer(int id) {
        return customerDao.getCustomer(id);
    }

    public void updateCustomer(Customer customer) {
        customerDao.updateCustomer(customer);
    }

    public void deleteCustomer(int id) {
        customerDao.deleteCustomer(id);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

}
