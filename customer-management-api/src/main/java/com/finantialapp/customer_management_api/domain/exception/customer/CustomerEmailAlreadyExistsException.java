package com.finantialapp.customer_management_api.domain.exception.customer;

public class CustomerEmailAlreadyExistsException extends RuntimeException {
    public CustomerEmailAlreadyExistsException() {

        super("Customer with the given email already exists.");
    }
}
