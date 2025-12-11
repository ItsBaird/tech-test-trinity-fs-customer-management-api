package com.finantialapp.customer_management_api.application.port.in;

import com.finantialapp.customer_management_api.domain.model.Customer;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerPatchRequest;

import java.util.List;

public interface CustomerServicePort {

    Customer findCustomerById(Long id);

    List<Customer> findAllCustomers();

    Customer saveCustomer(Customer customer);

    Customer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);

    Customer partialUpdateCustomer(Long id, CustomerPatchRequest patch);

}
