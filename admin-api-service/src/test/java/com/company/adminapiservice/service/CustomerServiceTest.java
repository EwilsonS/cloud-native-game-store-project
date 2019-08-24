package com.company.adminapiservice.service;

import com.company.adminapiservice.util.feign.CustomerClient;
import com.company.adminapiservice.util.messages.Customer;
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

public class CustomerServiceTest {


    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerClient customerClient;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        // configure mock objects
        setUpCustomerClientMock();

    }

    // tests addCustomer()
    @Test
    public void addCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Mari");
        customer.setLastName("Garcia");
        customer.setStreet("1000 Sturdivant Street");
        customer.setCity("Cary");
        customer.setZip("27511");
        customer.setEmail("marigarcia@gmail.com");
        customer.setPhone("919-374-2901");

        customer = customerService.addCustomer(customer);

        Customer customer1 = customerService.getCustomer(customer.getCustomerId());

        assertEquals(customer, customer1);
    }

    // tests getCustomer()
    @Test
    public void getCustomer() {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("Terry");
        customer.setLastName("DoByne");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Sedona");
        customer.setZip("86336");
        customer.setEmail("terrydobyne@yahoo.com");
        customer.setPhone("724-879-9234");

        Customer customer1 = customerService.getCustomer(customer.getCustomerId());

        assertEquals(customer, customer1);
    }

    // tests getAllCustomers()
    @Test
    public void findAllCustomers() {

        Customer customer = new Customer();
        customer.setFirstName("Terry");
        customer.setLastName("DoByne");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Sedona");
        customer.setZip("86336");
        customer.setEmail("terrydobyne@yahoo.com");
        customer.setPhone("724-879-9234");

        customerService.addCustomer(customer);

        Customer customer3 = new Customer();
        customer3.setFirstName("Mari");
        customer3.setLastName("Garcia");
        customer3.setStreet("1000 Sturdivant Street");
        customer3.setCity("Cary");
        customer3.setZip("27511");
        customer3.setEmail("marigarcia@gmail.com");
        customer3.setPhone("919-374-2901");

        customerService.addCustomer(customer);

        List<Customer> fromService = customerService.getAllCustomers();

        assertEquals(2, fromService.size());

    }

    // tests deleteCustomer()
    @Test
    public void deleteCustomer() {
        Customer customer = customerService.getCustomer(1);
        customerService.deleteCustomer(1);
        ArgumentCaptor<Integer> postCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(customerClient).deleteCustomer(postCaptor.capture());
        assertEquals(customer.getCustomerId(), postCaptor.getValue().intValue());
    }

    // tests updateCustomer()
    @Test
    public void updateCustomer() {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("Terry");
        customer.setLastName("DoByne");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Sedona");
        customer.setZip("86336");
        customer.setEmail("msterryclassroom4@yahoo.com");// instead of terrydobyne@yahoo.com
        customer.setPhone("724-879-9234");

        customerService.updateCustomer(customer.getCustomerId(), customer);
        ArgumentCaptor<Customer> postCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerClient).updateCustomer(any(Integer.class), postCaptor.capture());
        assertEquals(customer.getEmail(), postCaptor.getValue().getEmail());

    }

    // tests if will return null if try to get customer with non-existent id
    @Test
    public void getCustomerWithNonExistentId() {
        Customer customer = customerService.getCustomer(500);
        assertNull(customer);
    }

    // Create mocks

    public void setUpCustomerClientMock() {

        Customer customer = new Customer();
        customer.setFirstName("Terry");
        customer.setLastName("DoByne");
        customer.setStreet("2380 W US Hwy 89");
        customer.setCity("Sedona");
        customer.setZip("86336");
        customer.setEmail("terrydobyne@yahoo.com");
        customer.setPhone("724-879-9234");

        Customer customer2 = new Customer();
        customer2.setCustomerId(1);
        customer2.setFirstName("Terry");
        customer2.setLastName("DoByne");
        customer2.setStreet("2380 W US Hwy 89");
        customer2.setCity("Sedona");
        customer2.setZip("86336");
        customer2.setEmail("terrydobyne@yahoo.com");
        customer2.setPhone("724-879-9234");

        Customer customer3 = new Customer();
        customer3.setFirstName("Mari");
        customer3.setLastName("Garcia");
        customer3.setStreet("1000 Sturdivant Street");
        customer3.setCity("Cary");
        customer3.setZip("27511");
        customer3.setEmail("marigarcia@gmail.com");
        customer3.setPhone("919-374-2901");

        Customer customer4 = new Customer();
        customer4.setCustomerId(2);
        customer4.setFirstName("Mari");
        customer4.setLastName("Garcia");
        customer4.setStreet("1000 Sturdivant Street");
        customer4.setCity("Cary");
        customer4.setZip("27511");
        customer4.setEmail("marigarcia@gmail.com");
        customer4.setPhone("919-374-2901");

        doReturn(customer2).when(customerClient).addCustomer(customer);
        doReturn(customer4).when(customerClient).addCustomer(customer3);
        doReturn(customer2).when(customerClient).getCustomer(1);
        doReturn(customer4).when(customerClient).getCustomer(2);

        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer2);
        customerList.add(customer4);

        doReturn(customerList).when(customerClient).getAllCustomers();

    }

}
