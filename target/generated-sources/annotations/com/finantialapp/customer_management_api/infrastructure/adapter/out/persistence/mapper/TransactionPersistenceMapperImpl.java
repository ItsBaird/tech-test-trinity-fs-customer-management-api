package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper;

import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-14T18:18:31-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class TransactionPersistenceMapperImpl implements TransactionPersistenceMapper {

    @Override
    public TransactionEntity toTransactionEntity(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setId( transaction.getId() );
        transactionEntity.setTransactionType( transaction.getTransactionType() );
        transactionEntity.setAmount( transaction.getAmount() );
        transactionEntity.setSourceAccountId( transaction.getSourceAccountId() );
        transactionEntity.setDestinationAccountId( transaction.getDestinationAccountId() );
        transactionEntity.setTransactionDate( transaction.getTransactionDate() );

        return transactionEntity;
    }

    @Override
    public Transaction toTransaction(TransactionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        TransactionType transactionType = null;
        BigDecimal amount = null;
        Long sourceAccountId = null;
        Long destinationAccountId = null;
        LocalDateTime transactionDate = null;

        id = entity.getId();
        transactionType = entity.getTransactionType();
        amount = entity.getAmount();
        sourceAccountId = entity.getSourceAccountId();
        destinationAccountId = entity.getDestinationAccountId();
        transactionDate = entity.getTransactionDate();

        Transaction transaction = new Transaction( id, transactionType, amount, sourceAccountId, destinationAccountId, transactionDate );

        return transaction;
    }

    @Override
    public List<Transaction> toTransactionList(List<TransactionEntity> entittyList) {
        if ( entittyList == null ) {
            return null;
        }

        List<Transaction> list = new ArrayList<Transaction>( entittyList.size() );
        for ( TransactionEntity transactionEntity : entittyList ) {
            list.add( toTransaction( transactionEntity ) );
        }

        return list;
    }
}
