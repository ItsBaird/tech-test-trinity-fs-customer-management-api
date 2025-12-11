package com.finantialapp.customer_management_api.domain.exception.customer;

public class CustomerInvalidDeleteException extends RuntimeException {
    public CustomerInvalidDeleteException() {

        super("The customer cannot be deleted because they have associated accounts.");
    }
}
