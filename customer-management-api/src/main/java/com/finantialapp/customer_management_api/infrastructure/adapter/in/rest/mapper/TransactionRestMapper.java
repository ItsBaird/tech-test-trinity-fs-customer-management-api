package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper;

import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionRestMapper {

    Transaction toTransaction(TransactionCreateRequest request);

    default TransactionResponse toTransactionResponse(
            Transaction transaction,
            String sourceAccountNumber,
            String destinationAccountNumber) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .sourceAccountNumber(sourceAccountNumber)
                .destinationAccountNumber(destinationAccountNumber)
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}