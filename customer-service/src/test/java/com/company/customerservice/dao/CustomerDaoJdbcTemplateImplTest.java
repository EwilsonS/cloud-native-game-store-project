package com.company.customerservice.dao;

import com.company.customerservice.exception.NotFoundException;
import com.company.customerservice.model.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerDaoJdbcTemplateImplTest {

    @Autowired
    CustomerDao customerDao;

    // clear customer table in database
    @Before
    public void setUp() throws Exception {
        List<Customer> customers = customerDao.getAllCustomers();
        for (Customer c : customers) {
            customerDao.deleteCustomer(c.getCustomerId());
        }
    }

    // tests addCustomer(), getCustomer() and deleteCustomer()
    @Test
    public void addGetDeleteCustomer() {

        Customer customer = new Customer();
        customer.setFirstName("Terry");
        customer.setLastName("DoByne");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Sedona");
        customer.setZip("86336");
        customer.setEmail("terrydobyne@yahoo.com");
        customer.setPhone("724-879-9234");

        customer = customerDao.addCustomer(customer);

        Customer customer1 = customerDao.getCustomer(customer.getCustomerId());
        assertEquals(customer, customer1);

        customerDao.deleteCustomer(customer.getCustomerId());
        customer1 = customerDao.getCustomer(customer.getCustomerId());
        assertNull(customer1);
    }

    // tests if will return null if try to get customer with non-existent id
    @Test
    public void getCustomerWithNonExistentId() {
        Customer customer = customerDao.getCustomer(500);
        assertNull(customer);
    }

    // tests if will throw exception if id provided does not exist when trying to delete customer
    @Test(expected  = NotFoundException.class)
    public void deleteCustomerWithNonExistentId() {

        customerDao.deleteCustomer(500);

    }

    // tests updateCustomer()
    @Test
    public void updateConsole() {

        Customer customer = new Customer();
        customer.setFirstName("Terry");
        customer.setLastName("DoByne");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Sedona");
        customer.setZip("86336");
        customer.setEmail("terrydobyne@yahoo.com");
        customer.setPhone("724-879-9234");

        customer = customerDao.addCustomer(customer);

        customer.setEmail("msterryclassroom4@yahoo.com");

        customerDao.updateCustomer(customer);

        Customer customer1 = customerDao.getCustomer(customer.getCustomerId());
        assertEquals(customer, customer1);
    }

    // tests if will throw exception if id provided does not exist when trying to update customer
    @Test(expected  = IllegalArgumentException.class)
    public void updateCustomerWithIllegalArgumentException() {

        Customer customer = new Customer();
        customer.setCustomerId(500);
        customer.setFirstName("Terry");
        customer.setLastName("DoByne");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Sedona");
        customer.setZip("86336");
        customer.setEmail("terrydobyne@yahoo.com");
        customer.setPhone("724-879-9234");

        customerDao.updateCustomer(customer);

    }

    // tests getAllCustomers()
    @Test
    public void getAllCustomers() {

        Customer customer = new Customer();
        customer.setFirstName("Terry");
        customer.setLastName("DoByne");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Sedona");
        customer.setZip("86336");
        customer.setEmail("terrydobyne@yahoo.com");
        customer.setPhone("724-879-9234");

        customerDao.addCustomer(customer);

        customer = new Customer();
        customer.setFirstName("Mari");
        customer.setLastName("Garcia");
        customer.setStreet("1000 Sturdivant Street");
        customer.setCity("Cary");
        customer.setZip("27511");
        customer.setEmail("marigarcia@gmail.com");
        customer.setPhone("919-374-2901");

        customerDao.addCustomer(customer);

        List<Customer> cList = customerDao.getAllCustomers();
        assertEquals(2, cList.size());
    }

}
