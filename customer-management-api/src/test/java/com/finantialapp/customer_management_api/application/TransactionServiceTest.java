package com.finantialapp.customer_management_api.application;

import com.finantialapp.customer_management_api.application.port.out.AccountPersistencePort;
import com.finantialapp.customer_management_api.application.port.out.TransactionPersistencePort;
import com.finantialapp.customer_management_api.application.service.TransactionService;
import com.finantialapp.customer_management_api.domain.enums.AccountState;
import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import com.finantialapp.customer_management_api.domain.exception.account.AccountNotFoundException;
import com.finantialapp.customer_management_api.domain.exception.account.InsufficientBalanceException;
import com.finantialapp.customer_management_api.domain.exception.account.InvalidAccountStateException;
import com.finantialapp.customer_management_api.domain.exception.transaction.TransactionNotFoundException;
import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionPersistencePort transactionPersistencePort;

    @Mock
    private AccountPersistencePort accountPersistencePort;

    @InjectMocks
    private TransactionService transactionService;

    private Account source;
    private Account dest;
    private Transaction transaction;

    @BeforeEach
    void setup() {
        source = new Account();
        source.setId(1L);
        source.setBalance(new BigDecimal("100000"));
        source.setAccountState(AccountState.ACTIVA);
        source.setGmfExempt(false);

        dest = new Account();
        dest.setId(2L);
        dest.setBalance(new BigDecimal("200000"));
        dest.setAccountState(AccountState.ACTIVA);
        dest.setGmfExempt(true);

        transaction = new Transaction(
                10L,
                TransactionType.CONSIGNACION,
                new BigDecimal("50000"),
                1L,
                2L,
                null
        );
    }


    @Test
    @DisplayName("findTransactionById - Should return transaction when exists")
    void findTransactionById_success() {
        when(transactionPersistencePort.findById(10L)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.findTransactionById(10L);

        assertEquals(10L, result.getId());
    }

    @Test
    @DisplayName("findTransactionById - Should throw exception when not found")
    void findTransactionById_notFound() {
        when(transactionPersistencePort.findById(10L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.findTransactionById(10L));
    }

    @Test
    @DisplayName("findAllTransactions - Should return all transactions")
    void findAllTransactions_success() {
        when(transactionPersistencePort.findAll()).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.findAllTransactions();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("saveTransaction - Should save consignacion successfully")
    void saveTransaction_consignacion_success() {
        transaction.setTransactionType(TransactionType.CONSIGNACION);

        when(accountPersistencePort.findById(2L)).thenReturn(Optional.of(dest));
        when(transactionPersistencePort.save(any())).thenReturn(transaction);

        Transaction result = transactionService.saveTransaction(transaction);

        assertEquals(new BigDecimal("250000"), dest.getBalance());
        verify(accountPersistencePort).save(dest);
    }

    @Test
    @DisplayName("saveTransaction - Should throw AccountNotFoundException for consignacion")
    void saveTransaction_consignacion_accountNotFound() {
        transaction.setTransactionType(TransactionType.CONSIGNACION);

        when(accountPersistencePort.findById(2L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> transactionService.saveTransaction(transaction));
    }


    @Test
    @DisplayName("saveTransaction - Should save retiro successfully with GMF applied")
    void saveTransaction_retiro_success_withGMF() {
        transaction.setTransactionType(TransactionType.RETIRO);
        transaction.setAmount(new BigDecimal("50000"));

        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(source));
        when(transactionPersistencePort.save(any())).thenReturn(transaction);

        Transaction result = transactionService.saveTransaction(transaction);

        BigDecimal expected = new BigDecimal("49800");

        assertEquals(0, source.getBalance().compareTo(expected)); // ✔ compara valor, ignora escala
        verify(accountPersistencePort).save(source);
    }



    @Test
    @DisplayName("saveTransaction - Should save retiro successfully without GMF for exempt account")
    void saveTransaction_retiro_exempt_noGMF() {

        source.setGmfExempt(true);

        transaction.setTransactionType(TransactionType.RETIRO);
        transaction.setAmount(new BigDecimal("50000"));

        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(source));
        when(transactionPersistencePort.save(any())).thenReturn(transaction);

        Transaction result = transactionService.saveTransaction(transaction);

        // No aplica GMF → solo se descuenta el monto exacto
        assertEquals(new BigDecimal("50000"), transaction.getAmount());
        assertEquals(new BigDecimal("50000"), source.getBalance()); // 100000 - 50000
        verify(accountPersistencePort).save(source);
    }

    @Test
    @DisplayName("saveTransaction - Should throw InsufficientBalanceException for retiro")
    void saveTransaction_retiro_insufficientBalance() {
        transaction.setTransactionType(TransactionType.RETIRO);
        transaction.setAmount(new BigDecimal("200000"));

        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(source));

        assertThrows(InsufficientBalanceException.class,
                () -> transactionService.saveTransaction(transaction));
    }

    @Test
    @DisplayName("saveTransaction - Should throw InvalidAccountStateException for retiro on inactive account")
    void saveTransaction_retiro_inactiveAccount() {
        source.setAccountState(AccountState.INACTIVA);

        transaction.setTransactionType(TransactionType.RETIRO);

        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(source));

        assertThrows(InvalidAccountStateException.class,
                () -> transactionService.saveTransaction(transaction));
    }

    @Test
    @DisplayName("saveTransaction - Should save transferencia successfully")
    void saveTransaction_transfer_success() {
        transaction.setTransactionType(TransactionType.TRANSFERENCIA);
        transaction.setAmount(new BigDecimal("50000"));

        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(source));
        when(accountPersistencePort.findById(2L)).thenReturn(Optional.of(dest));
        when(transactionPersistencePort.save(any())).thenReturn(transaction);

        Transaction result = transactionService.saveTransaction(transaction);

        BigDecimal expectedSource = new BigDecimal("49800");
        BigDecimal expectedDest = new BigDecimal("250000");

        assertEquals(0, source.getBalance().compareTo(expectedSource)); // ✔
        assertEquals(0, dest.getBalance().compareTo(expectedDest));     // ✔

        verify(accountPersistencePort).save(source);
        verify(accountPersistencePort).save(dest);
    }



    @Test
    @DisplayName("saveTransaction - Should throw InvalidAccountStateException when source account is canceled")
    void saveTransaction_transfer_sourceInactive() {
        source.setAccountState(AccountState.CANCELADA);

        transaction.setTransactionType(TransactionType.TRANSFERENCIA);

        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(source));
        when(accountPersistencePort.findById(2L)).thenReturn(Optional.of(dest)); //

        assertThrows(InvalidAccountStateException.class,
                () -> transactionService.saveTransaction(transaction));
    }


    @Test
    @DisplayName("saveTransaction - Should throw InvalidAccountStateException for transferencia to inactive destination")
    void saveTransaction_transfer_destInactive() {
        dest.setAccountState(AccountState.INACTIVA);

        transaction.setTransactionType(TransactionType.TRANSFERENCIA);

        when(accountPersistencePort.findById(1L)).thenReturn(Optional.of(source));
        when(accountPersistencePort.findById(2L)).thenReturn(Optional.of(dest));

        assertThrows(InvalidAccountStateException.class,
                () -> transactionService.saveTransaction(transaction));
    }
}
