package com.finantialapp.customer_management_api.application.port.in;


import com.finantialapp.customer_management_api.domain.model.Transaction;

import java.util.List;

public interface TransactionServicePort {

    Transaction findTransactionById(Long id);

    List<Transaction> findAllTransactions();

    Transaction saveTransaction(Transaction transaction);

}
