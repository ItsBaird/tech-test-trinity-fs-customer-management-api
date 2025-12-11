package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence;

import com.finantialapp.customer_management_api.application.port.out.AccountPersistencePort;
import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.AccountEntity;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper.AccountPersistenceMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository.AccountRepository;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountPersistencePort {

    private final AccountRepository accountRepository;
    private final AccountPersistenceMapper mapper;
    private final CustomerRepository customerRepository;

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id)
                .map(mapper::toAccount);
    }



    @Override
    public List<Account> findAll() {
        return mapper.toAccountList(accountRepository.findAll());
    }

    @Override
    public Account save(Account account) {

        CustomerEntity customerEntity = customerRepository.findById(account.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        AccountEntity entity = mapper.toAccountEntity(account);

        // Asignar relación correctamente
        entity.setCustomer(customerEntity);

        AccountEntity saved = accountRepository.save(entity);

        return mapper.toAccount(saved);
    }





    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public boolean existsByAccountNumber(Long accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> findByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(mapper::toAccount)
                .toList();
    }
}
