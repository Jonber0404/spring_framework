package com.yrgo.client;

import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class SimpleClient {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");

        CustomerManagementService customerService =
                container.getBean("customerManagement", CustomerManagementService.class);

        List<Customer> customerList = customerService.getAllCustomers();

        for (Customer customer: customerList) {
            System.out.println(customer.toString());
        }
    }
}
