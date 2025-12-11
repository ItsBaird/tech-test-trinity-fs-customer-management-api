package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.validation;

import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TransactionRequestValidator implements ConstraintValidator<ValidTransactionRequest, TransactionCreateRequest> {

    @Override
    public boolean isValid(TransactionCreateRequest request, ConstraintValidatorContext context) {

        if (request == null) {
            return true;
        }

        TransactionType type = request.getTransactionType();

        if (type == null) {
            return addError(context, "Transaction type must not be null");
        }

        switch (type) {

            case CONSIGNACION:
                return validateConsignacion(request, context);

            case RETIRO:
                return validateRetiro(request, context);

            case TRANSFERENCIA:
                return validateTransferencia(request, context);

            default:
                return addError(context, "Unsupported transaction type");
        }
    }

    private boolean validateConsignacion(TransactionCreateRequest request, ConstraintValidatorContext context) {

        if (request.getDestinationAccountId() == null) {
            return addError(context, "destinationAccountId is required for CONSIGNACION");
        }

        if (request.getSourceAccountId() != null) {
            return addError(context, "sourceAccountId must be null for CONSIGNACION");
        }

        return true;
    }

    private boolean validateRetiro(TransactionCreateRequest request, ConstraintValidatorContext context) {

        if (request.getSourceAccountId() == null) {
            return addError(context, "sourceAccountId is required for RETIRO");
        }

        if (request.getDestinationAccountId() != null) {
            return addError(context, "destinationAccountId must be null for RETIRO");
        }

        return true;
    }

    private boolean validateTransferencia(TransactionCreateRequest request, ConstraintValidatorContext context) {

        Long source = request.getSourceAccountId();
        Long destination = request.getDestinationAccountId();

        if (source == null || destination == null) {
            return addError(context, "Both accounts are required for TRANSFERENCIA");
        }

        if (source.equals(destination)) {
            return addError(context, "sourceAccountId and destinationAccountId must be different");
        }

        return true;
    }

    private boolean addError(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}
