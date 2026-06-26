package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper;

import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-25T23:25:31-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class TransactionRestMapperImpl implements TransactionRestMapper {

    @Override
    public Transaction toTransaction(TransactionCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        TransactionType transactionType = null;
        BigDecimal amount = null;
        Long sourceAccountId = null;
        Long destinationAccountId = null;

        transactionType = request.getTransactionType();
        amount = request.getAmount();
        sourceAccountId = request.getSourceAccountId();
        destinationAccountId = request.getDestinationAccountId();

        Long id = null;
        LocalDateTime transactionDate = null;

        Transaction transaction = new Transaction( id, transactionType, amount, sourceAccountId, destinationAccountId, transactionDate );

        return transaction;
    }
}
