package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response;

import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private long id;
    private IdentificationType identificationType;
    private  long identificationNumber;
    private String names;
    private String surnames;
    private String email;
    private LocalDate dateOfBirth;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;
}
