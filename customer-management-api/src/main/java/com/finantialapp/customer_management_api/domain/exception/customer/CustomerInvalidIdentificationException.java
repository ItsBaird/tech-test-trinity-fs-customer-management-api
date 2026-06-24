package com.finantialapp.customer_management_api.domain.exception.customer;

public class CustomerInvalidIdentificationException extends RuntimeException {
    public CustomerInvalidIdentificationException(String message) {
        super(message);
    }
}
