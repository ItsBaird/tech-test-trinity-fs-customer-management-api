package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.controller;

import com.finantialapp.customer_management_api.application.port.in.AccountServicePort;
import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.AccountRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.*;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.AccountResponse;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.CustomerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountRestAdapter {

    private final AccountServicePort accountServicePort;
    private final AccountRestMapper restMapper;

    @GetMapping("/api/getAll")
    public List<AccountResponse> findAllAccounts() {
        return restMapper.toAccountResponseList(accountServicePort.findAllAccounts());
    }

    @GetMapping("/api/getById/{id}")
    public AccountResponse findById(@PathVariable Long id) {
        return restMapper.toAccountResponse(accountServicePort.findAccountById(id));
    }

    @PostMapping("/api/create")
    public ResponseEntity<AccountResponse> saveAccount(@Valid @RequestBody AccountCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toAccountResponse(
                        accountServicePort.saveAccount(
                                restMapper.toAccount(request)
                        )));
    }

    @PutMapping("/api/update/{id}")
    public AccountResponse updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountUpdateRequest request
    ) {
        return restMapper.toAccountResponse(
                accountServicePort.updateAccount(
                        id,
                        restMapper.toAccount(request)
                )
        );
    }

    @PatchMapping("/api/update/{id}")
    public AccountResponse patchAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountPatchRequest request) {

        return restMapper.toAccountResponse(
                accountServicePort.partialUpdateAccount(id, request)
        );
    }





    @DeleteMapping("/api/delete/{id}")
    public void deteleAccount(@PathVariable Long id) {
        accountServicePort.deleteAccount(id);
    }
}
