package com.finantialapp.customer_management_api.application.port.out;

import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.AccountEntity;


import java.util.List;
import java.util.Optional;

public interface AccountPersistencePort {

    Optional<Account> findById(Long id);

    List<Account> findAll();

    Account save(Account account);

    void deleteById(Long id);

    boolean existsByAccountNumber(Long accountNumber);

    List<Account> findByCustomerId(Long customerId);

}
