package com.finantialapp.customer_management_api.domain.exception.transaction;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException()
    {
        super("Transaction not found");
    }
}
