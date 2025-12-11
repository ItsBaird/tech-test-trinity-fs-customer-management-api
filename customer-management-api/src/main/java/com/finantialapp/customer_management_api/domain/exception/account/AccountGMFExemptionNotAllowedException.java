package com.finantialapp.customer_management_api.domain.exception.account;

public class AccountGMFExemptionNotAllowedException extends RuntimeException {
    public AccountGMFExemptionNotAllowedException() {

        super("The customer already has a GMF-exempt account.");
    }
}
