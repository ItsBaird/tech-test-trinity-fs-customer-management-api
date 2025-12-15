package com.finantialapp.customer_management_api.domain.exception.customer;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException()
    {
        super("Customer with the given identification already exists.");
    }
}
