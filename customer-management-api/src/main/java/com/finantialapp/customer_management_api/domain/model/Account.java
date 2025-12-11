package com.finantialapp.customer_management_api.domain.model;

import com.finantialapp.customer_management_api.domain.enums.AccountState;
import com.finantialapp.customer_management_api.domain.enums.AccountType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class Account {

    private Long id;
    private AccountType accountType;
    private Long accountNumber;
    private AccountState accountState;
    private BigDecimal balance;
    private boolean gmfExempt;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;
    private Long customerId;



    // Getters
    public Long getId() {
        return id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public AccountState getAccountState() {
        return accountState;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isGmfExempt() {
        return gmfExempt;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public Long getCustomerId() {
        return customerId;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setGmfExempt(boolean gmfExempt) {
        this.gmfExempt = gmfExempt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
