package com.finantialapp.customer_management_api.application.service;

import com.finantialapp.customer_management_api.application.port.in.TransactionServicePort;
import com.finantialapp.customer_management_api.application.port.out.AccountPersistencePort;
import com.finantialapp.customer_management_api.application.port.out.TransactionPersistencePort;
import com.finantialapp.customer_management_api.domain.enums.AccountState;
import com.finantialapp.customer_management_api.domain.exception.account.AccountNotFoundException;
import com.finantialapp.customer_management_api.domain.exception.account.InsufficientBalanceException;
import com.finantialapp.customer_management_api.domain.exception.account.InvalidAccountStateException;
import com.finantialapp.customer_management_api.domain.exception.transaction.TransactionNotFoundException;
import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionServicePort {

    private final TransactionPersistencePort persistencePort;
    private final AccountPersistencePort accountPersistencePort;

    @Override
    public Transaction findTransactionById(Long id) {
        return persistencePort.findById(id).orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return persistencePort.findAll();
    }

    @Override
    @Transactional
    public Transaction saveTransaction(Transaction transaction) {

        switch (transaction.getTransactionType()) {

            case CONSIGNACION -> {
                Account dest = accountPersistencePort.findById(transaction.getDestinationAccountId())
                        .orElseThrow(AccountNotFoundException::new);

                validateAccountIsActive(dest);

                // Consignación no cobra GMF
                dest.setBalance(dest.getBalance().add(transaction.getAmount()));
                accountPersistencePort.save(dest);
            }

            case RETIRO -> {
                Account source = accountPersistencePort.findById(transaction.getSourceAccountId())
                        .orElseThrow(AccountNotFoundException::new);

                validateAccountIsActive(source);

                BigDecimal amountToDebit = applyGMFIfNeeded(source, transaction.getAmount());

                validateBalance(source, amountToDebit);

                source.setBalance(source.getBalance().subtract(amountToDebit));
                accountPersistencePort.save(source);
            }

            case TRANSFERENCIA -> {
                Account source = accountPersistencePort.findById(transaction.getSourceAccountId())
                        .orElseThrow(AccountNotFoundException::new);

                Account dest = accountPersistencePort.findById(transaction.getDestinationAccountId())
                        .orElseThrow(AccountNotFoundException::new);

                validateAccountIsActive(source);
                validateAccountIsActive(dest);

                BigDecimal amountToDebit = applyGMFIfNeeded(source, transaction.getAmount());

                validateBalance(source, amountToDebit);

                // Debitar (aplicando GMF si aplica)
                source.setBalance(source.getBalance().subtract(amountToDebit));
                accountPersistencePort.save(source);

                // Acreditar el valor completo al destino (no se descuenta GMF)
                dest.setBalance(dest.getBalance().add(transaction.getAmount()));
                accountPersistencePort.save(dest);
            }
        }

        transaction.setTransactionDate(LocalDateTime.now());
        return persistencePort.save(transaction);
    }

    private void validateAccountIsActive(Account acc) {
        if (acc.getAccountState() != AccountState.ACTIVA) {
            throw new InvalidAccountStateException(acc.getId());
        }
    }

    private void validateBalance(Account acc, BigDecimal amount) {
        if (acc.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(acc.getId());
        }
    }

    private BigDecimal applyGMFIfNeeded(Account account, BigDecimal amount) {

        if (account.isGmfExempt()) {
            return amount; // No aplica GMF
        }

        BigDecimal gmf = amount.multiply(new BigDecimal("0.004"));

        return amount.add(gmf);
    }


}

