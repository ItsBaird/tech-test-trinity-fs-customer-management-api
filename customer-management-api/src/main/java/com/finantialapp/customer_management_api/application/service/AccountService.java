package com.finantialapp.customer_management_api.application.service;

import com.finantialapp.customer_management_api.application.port.in.AccountServicePort;
import com.finantialapp.customer_management_api.application.port.out.AccountPersistencePort;
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
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountServicePort {

    private final AccountPersistencePort persistencePort;
    private final CustomerRepository customerRepository;
    private final AccountRestMapper restMapper;


    @Override
    public Account findAccountById(Long id) {
        return persistencePort.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public List<Account> findAllAccounts() {
        return persistencePort.findAll();
    }

    @Override
    public Account saveAccount(Account account) {

        //validar tipo de cuenta
        if (account.getAccountType() == null ||
                (!account.getAccountType().equals(AccountType.AHORROS)
                        && !account.getAccountType().equals(AccountType.CORRIENTE))) {

            throw new AccountInvalidTypeException(
                    account.getAccountType() != null ? account.getAccountType().name() : "null"
            );
        }

        //validar que el customer exista
        customerRepository.findById(account.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);

        //validar regla del GMF — Solo 1 cuenta exenta por cliente
        if (account.isGmfExempt()) {

            List<Account> customerAccounts = persistencePort.findByCustomerId(account.getCustomerId());

            boolean alreadyHasExempt = customerAccounts.stream()
                    .anyMatch(Account::isGmfExempt);

            if (alreadyHasExempt) {
                throw new AccountGMFExemptionNotAllowedException();
            }
        }

        account.setAccountState(AccountState.ACTIVA);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(ZonedDateTime.now());

        //generar número de cuenta único
        String prefix = account.getAccountType() == AccountType.AHORROS ? "53" : "33";
        String numberPart;
        do {
            numberPart = String.format("%08d", new Random().nextInt(100_000_000));
            account.setAccountNumber(Long.parseLong(prefix + numberPart));
        } while (persistencePort.existsByAccountNumber(account.getAccountNumber()));

        return persistencePort.save(account);
    }


    @Override
    public Account updateAccount(Long id, Account account) {

        //buscar si existe
        Account existing = persistencePort.findById(id)
                .orElseThrow(AccountNotFoundException::new);

        // -------------------------------
        //validar si no hubo cambios
        // -------------------------------
        boolean noChanges =
                Objects.equals(existing.getAccountState(), account.getAccountState()) &&
                        Objects.equals(existing.isGmfExempt(), account.isGmfExempt());

        if (noChanges) {
            //no modificar modifiedAt, no guardar
            return existing;
        }


        //validar GMFExempt
        boolean updatingToExempt = account.isGmfExempt();
        boolean alreadyExempt = existing.isGmfExempt();

        if (updatingToExempt && !alreadyExempt) {

            // obtener todas las cuentas del cliente
            List<Account> accounts = persistencePort.findByCustomerId(existing.getCustomerId());

            // verificar si ya existe otra cuenta con GMFExempt true
            boolean hasOtherExempt = accounts.stream()
                    .anyMatch(acc -> acc.isGmfExempt() && !acc.getId().equals(existing.getId()));

            if (hasOtherExempt) {
                throw new AccountGMFExemptionNotAllowedException();
            }
        }


        //validar cambio de estado

        if (account.getAccountState() != null) {

            AccountState newState = account.getAccountState();

            // validar cancelación con balance > 0
            if (newState == AccountState.CANCELADA
                    && existing.getBalance().compareTo(BigDecimal.ZERO) != 0) {

                throw new AccountCancellationNotAllowedException();
            }

            existing.setAccountState(newState);
        }

        // -------------------------------
        //actualizar gmfExempt
        // -------------------------------
        existing.setGmfExempt(account.isGmfExempt());

        // -------------------------------
        // registrar fecha modificación
        // -------------------------------
        existing.setModifiedAt(ZonedDateTime.now());


        return persistencePort.save(existing);
    }






    @Override
    public void deleteAccount(Long id) {

        if (persistencePort.findById(id).isEmpty()) {
            throw new AccountNotFoundException();
        }

        persistencePort.deleteById(id);

    }

    @Override
    public Account partialUpdateAccount(Long id, AccountPatchRequest patch) {

        // buscar cuenta actual
        Account existing = persistencePort.findById(id)
                .orElseThrow(AccountNotFoundException::new);

        // crear un clon para comparar después
        Account beforeUpdate = cloneAccount(existing);

        // validación -  si intenta cancelar cuenta con balance > 0
        if (patch.getAccountState() == AccountState.CANCELADA) {
            if (existing.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new AccountCancellationNotAllowedException();
            }
        }

        //validación GMF Exempt (solo si el patch lo envía)
        if (patch.getGmfExempt() != null && patch.getGmfExempt()) {

            List<Account> customerAccounts = persistencePort.findByCustomerId(existing.getCustomerId());

            boolean anotherExempt = customerAccounts.stream()
                    .anyMatch(acc -> !acc.getId().equals(id) && acc.isGmfExempt());

            if (anotherExempt) {
                throw new AccountGMFExemptionNotAllowedException();
            }
        }

        restMapper.updateAccountFromPatch(patch, existing);

        // validar si no hubo cambios
        boolean noChanges =
                Objects.equals(beforeUpdate.getAccountState(), existing.getAccountState()) &&
                        Objects.equals(beforeUpdate.isGmfExempt(), existing.isGmfExempt());

        if (noChanges) {
            //nada cambió - no tocar modifiedAt, no guardar
            return existing;
        }

        //registrar modificación solo si hubo cambios reales
        existing.setModifiedAt(ZonedDateTime.now());

        return persistencePort.save(existing);
    }

    private Account cloneAccount(Account source) {
        Account clone = new Account();
        clone.setId(source.getId());
        clone.setAccountType(source.getAccountType());
        clone.setAccountNumber(source.getAccountNumber());
        clone.setAccountState(source.getAccountState());
        clone.setBalance(source.getBalance());
        clone.setGmfExempt(source.isGmfExempt());
        clone.setCustomerId(source.getCustomerId());
        clone.setCreatedAt(source.getCreatedAt());
        clone.setModifiedAt(source.getModifiedAt());
        return clone;
    }


}
