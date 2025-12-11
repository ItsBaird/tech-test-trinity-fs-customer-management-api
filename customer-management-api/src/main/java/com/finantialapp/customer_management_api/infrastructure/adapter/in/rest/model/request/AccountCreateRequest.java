package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request;

import com.finantialapp.customer_management_api.domain.enums.AccountState;
import com.finantialapp.customer_management_api.domain.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateRequest {

    @NotNull(message = "Field 'accountState' must not be empty or null")
    private AccountType accountType;
    @NotNull(message = "Field 'gmfExempt' must not be empty or null")
    private boolean gmfExempt;
    @NotNull(message = "Field 'customerId' must not be empty or null")
    private Long customerId;
}
