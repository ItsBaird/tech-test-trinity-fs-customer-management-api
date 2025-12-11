package com.finantialapp.customer_management_api.domain.exception.customer;

public class CustomerUnderAgeException extends RuntimeException {
    public CustomerUnderAgeException() {

        super("Customer must be 18 years or older");
    }

}
