package com.finantialapp.customer_management_api.domain.exception.account;

public class AccountInvalidTypeException extends RuntimeException {
    public AccountInvalidTypeException(String accountType)
    {
        super("Error: The account type '" + accountType + "' is invalid.");
    }
}
