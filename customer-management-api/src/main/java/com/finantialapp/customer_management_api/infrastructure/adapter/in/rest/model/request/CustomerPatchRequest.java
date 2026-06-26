package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request;

import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerPatchRequest {

    private IdentificationType identificationType;
    @Size(max = 25, message = "Field 'identificationNumber' must not exceed 25 characters")
    private String identificationNumber;
    @Size(min = 2, message = "Field 'names' must have at least 2 characters")
    private String names;
    @Size(min = 2, message = "Field 'surnames' must have at least 2 characters")
    private String surnames;
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format")
    private String email;
    private LocalDate dateOfBirth;
}
