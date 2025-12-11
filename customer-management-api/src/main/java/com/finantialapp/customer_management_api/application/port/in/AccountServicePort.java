package com.finantialapp.customer_management_api.application.port.in;

import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountPatchRequest;

import java.util.List;

public interface AccountServicePort {

    Account findAccountById(Long id);

    List<Account> findAllAccounts();

    Account saveAccount(Account account);

    Account updateAccount(Long id, Account account);

    void deleteAccount(Long id);

    Account partialUpdateAccount(Long id, AccountPatchRequest patch);

}
