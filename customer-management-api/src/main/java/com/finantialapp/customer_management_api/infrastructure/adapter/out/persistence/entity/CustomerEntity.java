package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity;

import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private IdentificationType identificationType;
    private  String identificationNumber;
    private String names;
    private String surnames;
    private String email;
    private LocalDate dateOfBirth;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountEntity> accounts = new ArrayList<>();
}
