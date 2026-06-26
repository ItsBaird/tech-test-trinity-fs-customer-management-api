package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.controller;

import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.TransactionResponse;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.service.TransactionQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionRestAdapter {

    private final TransactionQueryService transactionQueryService;

    @GetMapping("/api/getAll")
    public List<TransactionResponse> findAllTransactions() {
        return transactionQueryService.findAllEnriched();
    }

    @GetMapping("/api/getById/{id}")
    public TransactionResponse findById(@PathVariable Long id) {
        return transactionQueryService.findEnrichedById(id);
    }

    @PostMapping("/api/create")
    public TransactionResponse createTransaction(
            @Valid @RequestBody TransactionCreateRequest request) {
        return transactionQueryService.saveAndEnrich(request);
    }
}