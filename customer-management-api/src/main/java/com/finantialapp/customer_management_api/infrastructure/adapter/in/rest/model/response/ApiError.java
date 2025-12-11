package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ApiError {

    private final int status;
    private final String message;
    private final ZonedDateTime timestamp;
}
