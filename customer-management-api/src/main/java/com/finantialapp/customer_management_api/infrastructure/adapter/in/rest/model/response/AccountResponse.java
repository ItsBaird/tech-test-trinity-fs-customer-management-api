package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response;

import com.finantialapp.customer_management_api.domain.enums.AccountState;
import com.finantialapp.customer_management_api.domain.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private long id;
    private AccountType accountType;
    private long accountNumber;
    private AccountState accountState;
    private BigDecimal balance;
    private boolean gmfExempt;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;
    private long customerId;
}
