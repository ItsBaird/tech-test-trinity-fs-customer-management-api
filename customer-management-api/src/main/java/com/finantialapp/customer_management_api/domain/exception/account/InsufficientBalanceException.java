package com.finantialapp.customer_management_api.domain.exception.account;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(Long id) {

        super("Account " + id + " has insufficient balance");
    }
}
