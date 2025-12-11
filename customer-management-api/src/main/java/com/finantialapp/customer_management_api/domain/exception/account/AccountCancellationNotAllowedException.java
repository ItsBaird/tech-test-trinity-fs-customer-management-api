package com.finantialapp.customer_management_api.domain.exception.account;

public class AccountCancellationNotAllowedException extends RuntimeException {
    public AccountCancellationNotAllowedException()
    {
        super("The account cannot be canceled because its balance is not zero.");
    }
}
