package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper;

import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountPatchRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountUpdateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.AccountResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T18:20:01-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class AccountRestMapperImpl implements AccountRestMapper {

    @Override
    public Account toAccount(AccountCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Account account = new Account();

        account.setCustomerId( request.getCustomerId() );
        account.setAccountType( request.getAccountType() );
        account.setGmfExempt( request.isGmfExempt() );

        return account;
    }

    @Override
    public Account toAccount(AccountUpdateRequest request) {
        if ( request == null ) {
            return null;
        }

        Account account = new Account();

        account.setAccountState( request.getAccountState() );
        account.setGmfExempt( request.isGmfExempt() );

        return account;
    }

    @Override
    public AccountResponse toAccountResponse(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountResponse.AccountResponseBuilder accountResponse = AccountResponse.builder();

        if ( account.getId() != null ) {
            accountResponse.id( account.getId() );
        }
        accountResponse.accountType( account.getAccountType() );
        if ( account.getAccountNumber() != null ) {
            accountResponse.accountNumber( account.getAccountNumber() );
        }
        accountResponse.accountState( account.getAccountState() );
        accountResponse.balance( account.getBalance() );
        accountResponse.gmfExempt( account.isGmfExempt() );
        accountResponse.createdAt( account.getCreatedAt() );
        accountResponse.modifiedAt( account.getModifiedAt() );
        if ( account.getCustomerId() != null ) {
            accountResponse.customerId( account.getCustomerId() );
        }

        return accountResponse.build();
    }

    @Override
    public List<AccountResponse> toAccountResponseList(List<Account> accountList) {
        if ( accountList == null ) {
            return null;
        }

        List<AccountResponse> list = new ArrayList<AccountResponse>( accountList.size() );
        for ( Account account : accountList ) {
            list.add( toAccountResponse( account ) );
        }

        return list;
    }

    @Override
    public void updateAccountFromPatch(AccountPatchRequest patch, Account account) {
        if ( patch == null ) {
            return;
        }

        if ( patch.getAccountState() != null ) {
            account.setAccountState( patch.getAccountState() );
        }
        if ( patch.getGmfExempt() != null ) {
            account.setGmfExempt( patch.getGmfExempt() );
        }
    }
}
