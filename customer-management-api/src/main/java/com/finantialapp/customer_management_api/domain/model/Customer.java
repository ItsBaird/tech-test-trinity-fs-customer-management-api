package com.finantialapp.customer_management_api.domain.model;

import com.finantialapp.customer_management_api.domain.enums.IdentificationType;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class Customer {
    private long id;
    private IdentificationType identificationType;
    private  long identificationNumber;
    private String names;
    private String surnames;
    private String email;
    private LocalDate dateOfBirth;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;

    public Customer(long id, IdentificationType identificationType, long identificationNumber, String names, String surnames, String email, LocalDate dateOfBirth, ZonedDateTime createdAt, ZonedDateTime modifiedAt) {
        this.id = id;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.names = names;
        this.surnames = surnames;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public Customer() {
    }

    // Getters
    public long getId() {
        return id;
    }

    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    public long getIdentificationNumber() {
        return identificationNumber;
    }

    public String getNames() {
        return names;
    }

    public String getSurnames() {
        return surnames;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setIdentificationType(IdentificationType identificationType) {
        this.identificationType = identificationType;
    }

    public void setIdentificationNumber(long identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
