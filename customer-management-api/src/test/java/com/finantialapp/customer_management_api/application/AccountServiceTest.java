package com.finantialapp.customer_management_api.application;


import com.finantialapp.customer_management_api.application.port.out.AccountPersistencePort;
import com.finantialapp.customer_management_api.application.service.AccountService;
import com.finantialapp.customer_management_api.domain.enums.AccountState;
import com.finantialapp.customer_management_api.domain.enums.AccountType;
import com.finantialapp.customer_management_api.domain.exception.account.AccountCancellationNotAllowedException;
import com.finantialapp.customer_management_api.domain.exception.account.AccountGMFExemptionNotAllowedException;
import com.finantialapp.customer_management_api.domain.exception.account.AccountInvalidTypeException;
import com.finantialapp.customer_management_api.domain.exception.account.AccountNotFoundException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerNotFoundException;
import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.AccountRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountPatchRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


public class AccountServiceTest {

    @Mock
    private AccountPersistencePort persistencePort;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRestMapper restMapper;

    @InjectMocks
    private AccountService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Account buildAccount() {
        Account acc = new Account();
        acc.setId(1L);
        acc.setCustomerId(10L);
        acc.setAccountType(AccountType.AHORROS);
        acc.setAccountState(AccountState.ACTIVA);
        acc.setBalance(BigDecimal.ZERO);
        acc.setGmfExempt(false);
        return acc;
    }


    @Test
    @DisplayName("findAccountById - Test for findAccountById method")
    void testFindAccountById_Success() {
        Account account = buildAccount();
        when(persistencePort.findById(1L)).thenReturn(Optional.of(account));

        Account result = service.findAccountById(1L);

        assertEquals(account, result);
    }

    @Test
    @DisplayName("findAccountById - Test for findAccountById method when account not found")
    void testFindAccountById_NotFound() {
        when(persistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> service.findAccountById(1L));
    }


    @Test
    @DisplayName("findAllAccounts - Test for findAllAccounts method")
    void testFindAllAccounts() {
        when(persistencePort.findAll()).thenReturn(List.of(buildAccount()));

        List<Account> result = service.findAllAccounts();

        assertEquals(1, result.size());
    }


    @Test
    @DisplayName("saveAccount - Test for saveAccount method")
    void testSaveAccount_Success() {
        Account acc = buildAccount();

        when(customerRepository.findById(10L)).thenReturn(Optional.of(new CustomerEntity()));
        when(persistencePort.findByCustomerId(10L)).thenReturn(List.of());
        when(persistencePort.existsByAccountNumber(anyLong())).thenReturn(false);
        when(persistencePort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Account saved = service.saveAccount(acc);

        assertNotNull(saved.getAccountNumber());
        assertEquals(AccountState.ACTIVA, saved.getAccountState());
        verify(persistencePort).save(any(Account.class));
    }

    @Test
    @DisplayName("saveAccount - Test for saveAccount method with invalid account type")
    void testSaveAccount_InvalidType() {
        Account acc = buildAccount();
        acc.setAccountType(null);

        assertThrows(AccountInvalidTypeException.class, () -> service.saveAccount(acc));
    }

    @Test
    @DisplayName("saveAccount - Test for saveAccount method when customer not found")
    void testSaveAccount_CustomerNotFound() {
        Account acc = buildAccount();

        when(customerRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> service.saveAccount(acc));
    }

    @Test
    @DisplayName("saveAccount - Test for saveAccount method when GMF exemption not allowed")
    void testSaveAccount_GMF_AlreadyHasExempt() {
        Account acc = buildAccount();
        acc.setGmfExempt(true);

        when(customerRepository.findById(10L)).thenReturn(Optional.of(new CustomerEntity()));

        Account other = buildAccount();
        other.setId(99L);
        other.setGmfExempt(true);

        when(persistencePort.findByCustomerId(10L)).thenReturn(List.of(other));

        assertThrows(AccountGMFExemptionNotAllowedException.class, () -> service.saveAccount(acc));
    }


    @Test
    @DisplayName("updateAccount - Test for updateAccount method with no changes")
    void testUpdateAccount_NoChanges_ReturnsExisting() {
        Account existing = buildAccount();
        Account input = buildAccount();

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));

        Account result = service.updateAccount(1L, input);

        assertEquals(existing, result);
        verify(persistencePort, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("updateAccount - Test for updateAccount method when GMF exemption not allowed")
    void testUpdateAccount_GMF_NotAllowed() {
        Account existing = buildAccount();
        existing.setId(1L);
        existing.setGmfExempt(false);

        Account update = buildAccount();
        update.setGmfExempt(true);

        Account other = buildAccount();
        other.setId(99L);
        other.setGmfExempt(true);

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        when(persistencePort.findByCustomerId(10L)).thenReturn(List.of(existing, other));

        assertThrows(AccountGMFExemptionNotAllowedException.class,
                () -> service.updateAccount(1L, update));
    }

    @Test
    @DisplayName("updateAccount - Test for updateAccount method when cancellation not allowed due to balance")
    void testUpdateAccount_CancelWithBalanceNotAllowed() {
        Account existing = buildAccount();
        existing.setBalance(BigDecimal.valueOf(100));

        Account update = buildAccount();
        update.setAccountState(AccountState.CANCELADA);

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(AccountCancellationNotAllowedException.class,
                () -> service.updateAccount(1L, update));
    }

    @Test
    @DisplayName("updateAccount - Test for updateAccount method successful update")
    void testUpdateAccount_Success() {
        Account existing = buildAccount();
        Account update = buildAccount();
        update.setGmfExempt(true);

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        when(persistencePort.findByCustomerId(10L)).thenReturn(List.of(existing));
        when(persistencePort.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        Account result = service.updateAccount(1L, update);

        assertTrue(result.isGmfExempt());
        verify(persistencePort).save(any(Account.class));
    }


    @Test
    @DisplayName("deleteAccount - Test for deleteAccount method successful deletion")
    void testDeleteAccount_Success() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(buildAccount()));

        service.deleteAccount(1L);

        verify(persistencePort).deleteById(1L);
    }

    @Test
    @DisplayName("deleteAccount - Test for deleteAccount method when account not found")
    void testDeleteAccount_NotFound() {
        when(persistencePort.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> service.deleteAccount(1L));
    }


    @Test
    @DisplayName("partialUpdateAccount - Test for partialUpdateAccount method with no changes")
    void testPartialUpdate_NoChanges() {
        Account existing = buildAccount();

        AccountPatchRequest patch = AccountPatchRequest.builder()
                .accountState(existing.getAccountState())
                .gmfExempt(existing.isGmfExempt())
                .build();

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));

        Account result = service.partialUpdateAccount(1L, patch);

        assertEquals(existing, result);
        verify(persistencePort, never()).save(any());
    }

    @Test
    @DisplayName("partialUpdateAccount - Test for partialUpdateAccount method when GMF exemption not allowed")
    void testPartialUpdate_GMF_NotAllowed() {
        Account existing = buildAccount();
        existing.setId(1L);

        Account other = buildAccount();
        other.setId(99L);
        other.setGmfExempt(true);

        AccountPatchRequest patch = AccountPatchRequest.builder()
                .gmfExempt(true)
                .build();

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        when(persistencePort.findByCustomerId(10L)).thenReturn(List.of(existing, other));

        assertThrows(AccountGMFExemptionNotAllowedException.class,
                () -> service.partialUpdateAccount(1L, patch));
    }

    @Test
    @DisplayName("partialUpdateAccount - Test for partialUpdateAccount method successful update")
    void testPartialUpdate_Success() {
        Account existing = buildAccount();
        AccountPatchRequest patch = AccountPatchRequest.builder()
                .gmfExempt(true)
                .build();

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        when(persistencePort.findByCustomerId(10L)).thenReturn(List.of(existing));

        doAnswer(inv -> {
            AccountPatchRequest p = inv.getArgument(0);
            Account a = inv.getArgument(1);
            a.setGmfExempt(p.getGmfExempt());
            return null;
        }).when(restMapper).updateAccountFromPatch(any(), any());

        when(persistencePort.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        Account result = service.partialUpdateAccount(1L, patch);

        assertTrue(result.isGmfExempt());
        verify(persistencePort).save(any());
    }
}
