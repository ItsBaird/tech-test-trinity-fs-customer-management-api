package com.finantialapp.customer_management_api.application.port.out;


import com.finantialapp.customer_management_api.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionPersistencePort {
    Optional<Transaction> findById(Long id);

    List<Transaction> findAll();

    Transaction save(Transaction transaction);

}
