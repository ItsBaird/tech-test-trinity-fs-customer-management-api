package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request;

import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreateRequest {

    @NotNull(message = "Field 'identificationType' must not be empty or null")
    private IdentificationType identificationType;
    @NotNull(message = "Field 'identificationNumber' must not be null")
    @Positive(message = "Field 'identificationNumber' must be positive")
    private  long identificationNumber;
    @NotBlank(message = "Field 'names' must not be empty or null")
    @Size(min = 2, message = "Field 'names' must have at least 2 characters")
    private String names;
    @NotBlank(message = "Field 'surnames' must not be empty or null")
    @Size(min = 2, message = "Field 'surnames' must have at least 2 characters")
    private String surnames;
    @NotBlank(message = "Field 'email' must not be empty or null")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format")
    private String email;
    @NotNull(message = "Field 'dateOfBirth' must not be null")
    private LocalDate dateOfBirth;
}
