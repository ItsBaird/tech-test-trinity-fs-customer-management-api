package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request;

import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.validation.ValidTransactionRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidTransactionRequest
public class TransactionCreateRequest {

    @NotNull(message = "Transaction type must not be null")
    private TransactionType transactionType;
    @NotNull(message = "Amount must not be null")
    private BigDecimal amount;
    private Long sourceAccountId;
    private Long destinationAccountId;

}
