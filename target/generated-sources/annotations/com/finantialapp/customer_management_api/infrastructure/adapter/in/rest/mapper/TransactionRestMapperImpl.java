package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper;

import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.TransactionResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:20:01-0500",
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

    @Override
    public TransactionResponse toTransactionResponse(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionResponse.TransactionResponseBuilder transactionResponse = TransactionResponse.builder();

        transactionResponse.id( transaction.getId() );
        transactionResponse.transactionType( transaction.getTransactionType() );
        transactionResponse.amount( transaction.getAmount() );
        transactionResponse.sourceAccountId( transaction.getSourceAccountId() );
        transactionResponse.destinationAccountId( transaction.getDestinationAccountId() );
        transactionResponse.transactionDate( transaction.getTransactionDate() );

        return transactionResponse.build();
    }

    @Override
    public List<TransactionResponse> toTransactionResponseList(List<Transaction> transactionList) {
        if ( transactionList == null ) {
            return null;
        }

        List<TransactionResponse> list = new ArrayList<TransactionResponse>( transactionList.size() );
        for ( Transaction transaction : transactionList ) {
            list.add( toTransactionResponse( transaction ) );
        }

        return list;
    }
}
