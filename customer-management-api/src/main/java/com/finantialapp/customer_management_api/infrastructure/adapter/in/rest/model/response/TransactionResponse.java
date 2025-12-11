package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response;

import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private LocalDateTime transactionDate;
}
