package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper;

import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountPersistenceMapper {

    @Mapping(target = "customer", ignore = true) // la asignamos manualmente
    AccountEntity toAccountEntity(Account account);

    @Mapping(target = "customerId", source = "customer.id")
    Account toAccount(AccountEntity entity);

    List<Account> toAccountList(List<AccountEntity> entittyList);
}
