package com.finantialapp.customer_management_api.domain.exception.account;

public class InvalidAccountStateException extends RuntimeException {
    public InvalidAccountStateException(Long id) {

        super("Account " + id + " is not active");
    }
}
