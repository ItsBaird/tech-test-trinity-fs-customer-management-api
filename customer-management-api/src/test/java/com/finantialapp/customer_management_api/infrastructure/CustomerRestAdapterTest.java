package com.finantialapp.customer_management_api.infrastructure;

import com.finantialapp.customer_management_api.application.port.in.CustomerServicePort;
import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import com.finantialapp.customer_management_api.domain.model.Customer;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.controller.CustomerRestAdapter;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.CustomerRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerPatchRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.CustomerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class CustomerRestAdapterTest {

    @Mock
    private CustomerServicePort customerServicePort;

    @Mock
    private CustomerRestMapper customerRestMapper;

    @InjectMocks
    private CustomerRestAdapter customerRestAdapter;



    @Test
    @DisplayName("getAll - Test for findAllCustomers method")
    void testFindAllCustomers() {

        Customer c = new Customer(1L, IdentificationType.CC, 123L, "Juan", "Perez",
                "jp@mail.com", LocalDate.now(), ZonedDateTime.now(), ZonedDateTime.now());

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .names("Juan")
                .build();

        Mockito.when(customerServicePort.findAllCustomers()).thenReturn(List.of(c));
        Mockito.when(customerRestMapper.toCustomerResponseList(List.of(c)))
                .thenReturn(List.of(response));

        List<CustomerResponse> result = customerRestAdapter.findAllCustomers();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        Mockito.verify(customerServicePort).findAllCustomers();
        Mockito.verify(customerRestMapper).toCustomerResponseList(List.of(c));
    }



    @Test
    @DisplayName("getById - Test for findById method")
    void testFindById() {

        Customer c = new Customer(1L, IdentificationType.CC, 123L, "Juan", "Perez",
                "jp@mail.com", LocalDate.now(), ZonedDateTime.now(), ZonedDateTime.now());

        CustomerResponse response = CustomerResponse.builder().id(1L).build();

        Mockito.when(customerServicePort.findCustomerById(1L)).thenReturn(c);
        Mockito.when(customerRestMapper.toCustomerResponse(c)).thenReturn(response);

        CustomerResponse result = customerRestAdapter.findById(1L);

        assertEquals(1L, result.getId());

        Mockito.verify(customerServicePort).findCustomerById(1L);
        Mockito.verify(customerRestMapper).toCustomerResponse(c);
    }


    @Test
    @DisplayName("create - Test for saveCustomer method")
    void testSaveCustomer() {

        CustomerCreateRequest req = CustomerCreateRequest.builder()
                .identificationType(IdentificationType.CC)
                .identificationNumber(123L)
                .names("Juan")
                .build();

        Customer domain = new Customer(1L, IdentificationType.CC, 123L, "Juan", "Perez",
                "jp@mail.com", LocalDate.now(), ZonedDateTime.now(), ZonedDateTime.now());

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .build();

        Mockito.when(customerRestMapper.toCustomer(req)).thenReturn(domain);
        Mockito.when(customerServicePort.saveCustomer(domain)).thenReturn(domain);
        Mockito.when(customerRestMapper.toCustomerResponse(domain)).thenReturn(response);

        var resultResponse = customerRestAdapter.saveCustomer(req);

        assertEquals(201, resultResponse.getStatusCode().value());
        assertEquals(1L, resultResponse.getBody().getId());

        Mockito.verify(customerRestMapper).toCustomer(req);
        Mockito.verify(customerServicePort).saveCustomer(domain);
        Mockito.verify(customerRestMapper).toCustomerResponse(domain);
    }

    // ======================================================
    // PUT UPDATE
    // ======================================================

    @Test
    @DisplayName("update - Test for updateCustomer method")
    void testUpdateCustomer() {

        CustomerCreateRequest req = CustomerCreateRequest.builder()
                .identificationType(IdentificationType.CC)
                .identificationNumber(123L)
                .names("Juan")
                .build();

        Customer mapped = new Customer();
        Customer updated = new Customer(1L, IdentificationType.CC, 123L,
                "Updated", "Updated", "upd@mail.com",
                LocalDate.now(), ZonedDateTime.now(), ZonedDateTime.now());

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .email("upd@mail.com")
                .build();

        Mockito.when(customerRestMapper.toCustomer(req)).thenReturn(mapped);
        Mockito.when(customerServicePort.updateCustomer(1L, mapped)).thenReturn(updated);
        Mockito.when(customerRestMapper.toCustomerResponse(updated)).thenReturn(response);

        CustomerResponse result = customerRestAdapter.updateCustomer(1L, req);

        assertEquals("upd@mail.com", result.getEmail());

        Mockito.verify(customerRestMapper).toCustomer(req);
        Mockito.verify(customerServicePort).updateCustomer(1L, mapped);
        Mockito.verify(customerRestMapper).toCustomerResponse(updated);
    }


    @Test
    @DisplayName("patch - Test for patchCustomer method")
    void testPatchCustomer() {

        CustomerPatchRequest req = CustomerPatchRequest.builder()
                .email("new@mail.com")
                .build();

        Customer updated = new Customer(1L, IdentificationType.CC, 123L,
                "Juan", "Perez", "new@mail.com",
                LocalDate.now(), ZonedDateTime.now(), ZonedDateTime.now());

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .email("new@mail.com")
                .build();

        Mockito.when(customerServicePort.partialUpdateCustomer(1L, req)).thenReturn(updated);
        Mockito.when(customerRestMapper.toCustomerResponse(updated)).thenReturn(response);

        CustomerResponse result = customerRestAdapter.patchCustomer(1L, req);

        assertEquals("new@mail.com", result.getEmail());

        Mockito.verify(customerServicePort).partialUpdateCustomer(1L, req);
        Mockito.verify(customerRestMapper).toCustomerResponse(updated);
    }



    @Test
    @DisplayName("delete - Test for deleteCustomer method")
    void testDeleteCustomer() {

        customerRestAdapter.deteleCustomer(1L);

        Mockito.verify(customerServicePort).deleteCustomer(1L);
    }



}
