package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper;

import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionPersistenceMapper {

    TransactionEntity toTransactionEntity(Transaction transaction);

    Transaction toTransaction(TransactionEntity entity);

    List<Transaction> toTransactionList(List<TransactionEntity> entittyList);
}
