package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request;

import com.finantialapp.customer_management_api.domain.enums.AccountState;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountPatchRequest {


    private AccountState accountState;
    private Boolean gmfExempt;
}
