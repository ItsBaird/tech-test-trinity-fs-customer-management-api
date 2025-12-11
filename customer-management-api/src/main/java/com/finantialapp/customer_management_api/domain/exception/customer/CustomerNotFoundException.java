package com.finantialapp.customer_management_api.domain.exception.customer;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException()
    {
        super("Customer not found");
    }
}
