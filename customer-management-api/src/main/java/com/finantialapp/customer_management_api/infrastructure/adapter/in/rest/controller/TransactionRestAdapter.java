package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.controller;

import com.finantialapp.customer_management_api.application.port.in.TransactionServicePort;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.TransactionRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionRestAdapter {

    private final TransactionServicePort transactionServicePort;
    private final TransactionRestMapper restMapper;

    @GetMapping("/api/getAll")
    public List<TransactionResponse> findAllTransactions() {
        return restMapper.toTransactionResponseList(transactionServicePort.findAllTransactions());
    }

    @GetMapping("/api/getById/{id}")
    public TransactionResponse findById(@PathVariable Long id) {
        return restMapper.toTransactionResponse(transactionServicePort.findTransactionById(id));
    }

    @PostMapping("/api/create")
    public TransactionResponse createTransaction(
            @Valid @RequestBody TransactionCreateRequest request
    ) {
        Transaction transaction = transactionServicePort.saveTransaction(
                restMapper.toTransaction(request)
        );

        return restMapper.toTransactionResponse(transaction);
    }



}
