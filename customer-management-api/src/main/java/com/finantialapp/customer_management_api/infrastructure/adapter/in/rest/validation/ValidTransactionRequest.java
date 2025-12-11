package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = TransactionRequestValidator.class)
public @interface ValidTransactionRequest {

    String message() default "Petición de transacción inválida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}