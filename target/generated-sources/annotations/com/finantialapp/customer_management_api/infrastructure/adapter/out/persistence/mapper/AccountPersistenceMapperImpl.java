package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper;

import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.AccountEntity;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-14T18:18:31-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class AccountPersistenceMapperImpl implements AccountPersistenceMapper {

    @Override
    public AccountEntity toAccountEntity(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setId( account.getId() );
        accountEntity.setAccountType( account.getAccountType() );
        accountEntity.setAccountNumber( account.getAccountNumber() );
        accountEntity.setAccountState( account.getAccountState() );
        accountEntity.setBalance( account.getBalance() );
        accountEntity.setGmfExempt( account.isGmfExempt() );
        accountEntity.setCreatedAt( account.getCreatedAt() );
        accountEntity.setModifiedAt( account.getModifiedAt() );

        return accountEntity;
    }

    @Override
    public Account toAccount(AccountEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Account account = new Account();

        account.setCustomerId( entityCustomerId( entity ) );
        account.setId( entity.getId() );
        account.setAccountType( entity.getAccountType() );
        account.setAccountNumber( entity.getAccountNumber() );
        account.setAccountState( entity.getAccountState() );
        account.setBalance( entity.getBalance() );
        account.setGmfExempt( entity.isGmfExempt() );
        account.setCreatedAt( entity.getCreatedAt() );
        account.setModifiedAt( entity.getModifiedAt() );

        return account;
    }

    @Override
    public List<Account> toAccountList(List<AccountEntity> entittyList) {
        if ( entittyList == null ) {
            return null;
        }

        List<Account> list = new ArrayList<Account>( entittyList.size() );
        for ( AccountEntity accountEntity : entittyList ) {
            list.add( toAccount( accountEntity ) );
        }

        return list;
    }

    private Long entityCustomerId(AccountEntity accountEntity) {
        CustomerEntity customer = accountEntity.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }
}
