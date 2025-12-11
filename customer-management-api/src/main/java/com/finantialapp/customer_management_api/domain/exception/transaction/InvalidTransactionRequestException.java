package com.finantialapp.customer_management_api.domain.exception.transaction;

public class InvalidTransactionRequestException extends RuntimeException {
    public InvalidTransactionRequestException(String message) {

      super(message);
    }
}
