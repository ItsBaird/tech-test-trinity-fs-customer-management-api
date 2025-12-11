package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence;

import com.finantialapp.customer_management_api.application.port.out.TransactionPersistencePort;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper.TransactionPersistenceMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionPersistencePort {

    private final TransactionRepository transactionRepository;
    private final TransactionPersistenceMapper mapper;

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id)
                .map(mapper::toTransaction);
    }

    @Override
    public List<Transaction> findAll() {
        return mapper.toTransactionList(transactionRepository.findAll());
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.toTransactionEntity(transaction);
        TransactionEntity saved = transactionRepository.save(entity);
        return mapper.toTransaction(saved);
    }
}
