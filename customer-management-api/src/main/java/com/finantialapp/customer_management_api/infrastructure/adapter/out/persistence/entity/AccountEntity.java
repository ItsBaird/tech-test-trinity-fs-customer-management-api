package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity;

import com.finantialapp.customer_management_api.domain.enums.AccountState;
import com.finantialapp.customer_management_api.domain.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private Long accountNumber;
    @Enumerated(EnumType.STRING)
    private AccountState accountState;
    private BigDecimal balance;
    private boolean gmfExempt;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;

    // RELACIÓN MANY-TO-ONE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;
}
