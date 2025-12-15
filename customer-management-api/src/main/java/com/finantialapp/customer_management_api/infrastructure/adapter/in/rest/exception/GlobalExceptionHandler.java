package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.exception;


import com.finantialapp.customer_management_api.domain.exception.account.*;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerAlreadyExistsException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerInvalidDeleteException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerNotFoundException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerUnderAgeException;
import com.finantialapp.customer_management_api.domain.exception.transaction.InvalidTransactionRequestException;
import com.finantialapp.customer_management_api.domain.exception.transaction.TransactionNotFoundException;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.ApiError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Customer
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {

        String errorMessage =
                Stream.concat(
                        ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage),
                        ex.getBindingResult().getGlobalErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                ).collect(Collectors.joining("; "));

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .timestamp(ZonedDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }



    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiError> handleCustomerNotFound(CustomerNotFoundException ex) {

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerUnderAgeException.class)
    public ResponseEntity<ApiError> handleCustomerUnderAgeException(CustomerUnderAgeException ex) {

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerInvalidDeleteException.class)
    public ResponseEntity<ApiError> handleCustomerInvalidDelete(CustomerInvalidDeleteException ex) {

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleCustomerAlreadyExists(CustomerAlreadyExistsException ex) {

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



    //Account
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFound(AccountNotFoundException ex) {

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountInvalidTypeException.class)
    public ResponseEntity<ApiError> handleAccountInvalidType(AccountInvalidTypeException ex) {

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidEnum(HttpMessageNotReadableException ex) {

        String message = ex.getMessage();

        if (message.contains("AccountType")) {
            ApiError error = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid value for accountType. Allowed: AHORROS, CORRIENTE",
                    ZonedDateTime.now()
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        if (message.contains("AccountState")) {
            ApiError error = new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid value for accountState. Allowed: ACTIVA, BLOQUEADA, CANCELADA",
                    ZonedDateTime.now()
            );
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid value in request body.",
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccountStateException.class)
    public ResponseEntity<ApiError> handleInvalidAccState(InvalidAccountStateException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiError> handleInsufficientBalance(InsufficientBalanceException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountCancellationNotAllowedException.class)
    public ResponseEntity<ApiError> handleAccountCancellationNotAllowed(AccountCancellationNotAllowedException ex) {

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountGMFExemptionNotAllowedException.class)
    public ResponseEntity<ApiError> handleAccountGMFExemptionNotAllowed(AccountGMFExemptionNotAllowedException ex) {

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



    //Transaction
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFound(TransactionNotFoundException ex) {

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTransactionRequestException.class)
    public ResponseEntity<ApiError> handleInvalidTransaction(InvalidTransactionRequestException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



    //Handler general para excepciones no controladas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
