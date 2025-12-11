package com.finantialapp.customer_management_api.infrastructure;


import com.finantialapp.customer_management_api.application.port.in.AccountServicePort;
import com.finantialapp.customer_management_api.domain.enums.AccountState;
import com.finantialapp.customer_management_api.domain.enums.AccountType;
import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.controller.AccountRestAdapter;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.AccountRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountPatchRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountUpdateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.AccountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class AccountRestAdapterTest {

    @Mock
    private AccountRestMapper accountRestMapper;

    @Mock
    private AccountServicePort accountServicePort;

    @InjectMocks
    private AccountRestAdapter accountRestAdapter;


    @Test
    @DisplayName("getAll - Test for findAllAccounts method")
    void testFindAllAccounts() {
        Account acc = buildAccount();

        AccountResponse resp = buildResponse();

        Mockito.when(accountServicePort.findAllAccounts()).thenReturn(List.of(acc));
        Mockito.when(accountRestMapper.toAccountResponseList(List.of(acc))).thenReturn(List.of(resp));

        List<AccountResponse> result = accountRestAdapter.findAllAccounts();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }


    @Test
    @DisplayName("getById - Test for findById method")
    void testFindById() {
        Account acc = buildAccount();
        AccountResponse resp = buildResponse();

        Mockito.when(accountServicePort.findAccountById(1L)).thenReturn(acc);
        Mockito.when(accountRestMapper.toAccountResponse(acc)).thenReturn(resp);

        AccountResponse result = accountRestAdapter.findById(1L);

        assertEquals(1L, result.getId());
    }


    @Test
    @DisplayName("save - Test for saveAccount method")
    void testSaveAccount() {
        AccountCreateRequest req = AccountCreateRequest.builder()
                .accountType(AccountType.AHORROS)
                .gmfExempt(true)
                .customerId(10L)
                .build();

        Account acc = buildAccount();
        AccountResponse resp = buildResponse();

        Mockito.when(accountRestMapper.toAccount(req)).thenReturn(acc);
        Mockito.when(accountServicePort.saveAccount(acc)).thenReturn(acc);
        Mockito.when(accountRestMapper.toAccountResponse(acc)).thenReturn(resp);

        var result = accountRestAdapter.saveAccount(req);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(1L, result.getBody().getId());
    }


    @Test
    @DisplayName("update - Test for updateAccount method")
    void testUpdateAccount() {
        AccountUpdateRequest req = AccountUpdateRequest.builder()
                .accountState(AccountState.ACTIVA)
                .gmfExempt(false)
                .build();

        Account acc = buildAccount();
        AccountResponse resp = buildResponse();

        Mockito.when(accountRestMapper.toAccount(req)).thenReturn(acc);
        Mockito.when(accountServicePort.updateAccount(eq(1L), eq(acc))).thenReturn(acc);
        Mockito.when(accountRestMapper.toAccountResponse(acc)).thenReturn(resp);

        AccountResponse result = accountRestAdapter.updateAccount(1L, req);

        assertEquals(1L, result.getId());
    }


    @Test
    @DisplayName("patch - Test for patchAccount method")
    void testPatchAccount() {

        AccountPatchRequest req = AccountPatchRequest.builder()
                .accountState(AccountState.INACTIVA)
                .gmfExempt(false)
                .build();


        Account accPatched = new Account();
        accPatched.setId(1L);
        accPatched.setAccountType(AccountType.AHORROS);
        accPatched.setAccountNumber(12345L);
        accPatched.setAccountState(AccountState.INACTIVA);
        accPatched.setBalance(new BigDecimal("1000"));
        accPatched.setGmfExempt(false);
        accPatched.setCreatedAt(ZonedDateTime.now());
        accPatched.setModifiedAt(ZonedDateTime.now());
        accPatched.setCustomerId(10L);


        AccountResponse respPatched = AccountResponse.builder()
                .id(1L)
                .accountType(AccountType.AHORROS)
                .accountNumber(12345L)
                .accountState(AccountState.INACTIVA)
                .balance(new BigDecimal("1000"))
                .gmfExempt(false)
                .createdAt(ZonedDateTime.now())
                .modifiedAt(ZonedDateTime.now())
                .customerId(10L)
                .build();


        Mockito.when(accountServicePort.partialUpdateAccount(eq(1L), eq(req)))
                .thenReturn(accPatched);

        Mockito.when(accountRestMapper.toAccountResponse(accPatched))
                .thenReturn(respPatched);


        AccountResponse result = accountRestAdapter.patchAccount(1L, req);


        assertEquals(AccountState.INACTIVA, result.getAccountState());
        assertEquals(false, result.isGmfExempt());
    }


    @Test
    @DisplayName("delete - Test for deleteAccount method")
    void testDeleteAccount() {
        accountRestAdapter.deteleAccount(1L);
        Mockito.verify(accountServicePort).deleteAccount(1L);
    }


    private Account buildAccount() {
        Account acc = new Account();
        acc.setId(1L);
        acc.setAccountType(AccountType.AHORROS);
        acc.setAccountNumber(12345L);
        acc.setAccountState(AccountState.ACTIVA);
        acc.setBalance(new BigDecimal("1000"));
        acc.setGmfExempt(true);
        acc.setCreatedAt(ZonedDateTime.now());
        acc.setModifiedAt(ZonedDateTime.now());
        acc.setCustomerId(10L);
        return acc;
    }

    private AccountResponse buildResponse() {
        return AccountResponse.builder()
                .id(1L)
                .accountType(AccountType.AHORROS)
                .accountNumber(12345L)
                .accountState(AccountState.ACTIVA)
                .balance(new BigDecimal("1000"))
                .gmfExempt(true)
                .createdAt(ZonedDateTime.now())
                .modifiedAt(ZonedDateTime.now())
                .customerId(10L)
                .build();
    }

}
