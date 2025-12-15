package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request;

import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPatchRequest {


    private IdentificationType identificationType;
    private String identificationNumber;
    @Size(min = 2, message = "Field 'names' must have at least 2 characters")
    private String names;
    @Size(min = 2, message = "Field 'surnames' must have at least 2 characters")
    private String surnames;
    @Email(message = "Field 'email' must be a valid email address")
    private String email;
    private LocalDate dateOfBirth;
}
