package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.service;

import com.finantialapp.customer_management_api.application.port.in.AccountServicePort;
import com.finantialapp.customer_management_api.application.port.in.TransactionServicePort;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.TransactionRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionQueryService {

    private final TransactionServicePort transactionServicePort;
    private final AccountServicePort accountServicePort;
    private final TransactionRestMapper restMapper;

    public List<TransactionResponse> findAllEnriched() {
        return transactionServicePort.findAllTransactions()
                .stream()
                .map(this::enrich)
                .toList();
    }

    public TransactionResponse findEnrichedById(Long id) {
        return enrich(transactionServicePort.findTransactionById(id));
    }

    public TransactionResponse saveAndEnrich(TransactionCreateRequest request) {
        Transaction tx = transactionServicePort.saveTransaction(
                restMapper.toTransaction(request));
        return enrich(tx);
    }

    private TransactionResponse enrich(Transaction tx) {
        String sourceNumber = tx.getSourceAccountId() != null
                ? accountServicePort.findAccountById(tx.getSourceAccountId())
                .getAccountNumber().toString()
                : null;

        String destNumber = tx.getDestinationAccountId() != null
                ? accountServicePort.findAccountById(tx.getDestinationAccountId())
                .getAccountNumber().toString()
                : null;

        return restMapper.toTransactionResponse(tx, sourceNumber, destNumber);
    }
}