package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.controller;

import com.finantialapp.customer_management_api.application.port.in.CustomerServicePort;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.CustomerRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerPatchRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.CustomerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerRestAdapter {

    private final CustomerServicePort customerServicePort;
    private final CustomerRestMapper restMapper;

    @GetMapping("/api/getAll")
    public List<CustomerResponse> findAllCustomers() {
        return restMapper.toCustomerResponseList(customerServicePort.findAllCustomers());
    }

    @GetMapping("/api/getById/{id}")
    public CustomerResponse findById(@PathVariable Long id) {
        return restMapper.toCustomerResponse(customerServicePort.findCustomerById(id));
    }

    @PostMapping("/api/create")
    public ResponseEntity<CustomerResponse> saveCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toCustomerResponse(
                        customerServicePort.saveCustomer(
                                restMapper.toCustomer(request)
                )));
    }

    @PutMapping("/api/update/{id}")
    public CustomerResponse updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerCreateRequest request) {
        return restMapper.toCustomerResponse(
                customerServicePort.updateCustomer(
                        id,
                        restMapper.toCustomer(request)
                ));
    }

    @PatchMapping("/api/update/{id}")
    public CustomerResponse patchCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerPatchRequest request) {

        return restMapper.toCustomerResponse(
                customerServicePort.partialUpdateCustomer(id, request)
        );
    }


    @DeleteMapping("/api/delete/{id}")
    public void deteleCustomer(@PathVariable Long id) {
        customerServicePort.deleteCustomer(id);
    }

}
