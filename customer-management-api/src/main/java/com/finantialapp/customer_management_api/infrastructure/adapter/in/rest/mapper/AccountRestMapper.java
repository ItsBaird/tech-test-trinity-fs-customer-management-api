package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper;

import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountPatchRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.AccountUpdateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.AccountResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountRestMapper {

    @Mapping(target = "customerId", source = "request.customerId")
    Account toAccount(AccountCreateRequest request);

    Account toAccount(AccountUpdateRequest request);

    AccountResponse toAccountResponse(Account account);

    List<AccountResponse> toAccountResponseList(List<Account> accountList);

    void updateAccountFromPatch(AccountPatchRequest patch, @MappingTarget Account account);

}
