package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request;

import com.finantialapp.customer_management_api.domain.enums.AccountState;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateRequest {

    @NotNull(message = "Field 'accountState' must not be empty or null")
    private AccountState accountState;
    @NotNull(message = "Field 'gmfExempt' must not be empty or null")
    private boolean gmfExempt;
}
