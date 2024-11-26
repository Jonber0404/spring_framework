package com.yrgo.integrationstests;

import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration( { "/other-tiers.xml", "/datasource-test.xml" } )
public class ManagingCustomersIntegrationTest {

    @Autowired
    private CustomerManagementService customers;

    @Test
    public void testCreatingCustomer(){

        customers.newCustomer(new Customer
                ("500", "yrgo", "cust.omer@yrgo.se", "12345", "notes"));

        List<Customer> customerList = customers.getAllCustomers();

        assertTrue(customerList.stream().anyMatch(customer -> "500".equals(customer.getCustomerId())));
    }

    @Test
    public void testFindingCustomerById() throws RecordNotFoundException, CustomerNotFoundException {

        Customer customer = new Customer
                ("600", "yrgo1", "cust1.omer@yrgo.se", "123456", "notes");
        customers.newCustomer(customer);

        String id = "600";

        assertEquals(customers.findCustomerById(id), customer);

    }
}
