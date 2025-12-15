package com.finantialapp.customer_management_api.application;

import com.finantialapp.customer_management_api.application.port.out.AccountPersistencePort;
import com.finantialapp.customer_management_api.application.port.out.CustomerPersistencePort;
import com.finantialapp.customer_management_api.application.service.CustomerService;
import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerInvalidDeleteException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerNotFoundException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerUnderAgeException;
import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.domain.model.Customer;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.CustomerRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerPatchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerPersistencePort persistencePort;

    @Mock
    private AccountPersistencePort accountPersistencePort;

    @Mock
    private CustomerRestMapper restMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer adult;
    private Customer underAge;

    @BeforeEach
    void setup() {

        adult = new Customer();
        adult.setId(1);
        adult.setNames("John");
        adult.setSurnames("Doe");
        adult.setEmail("john@doe.com");
        adult.setIdentificationType(IdentificationType.CC);
        adult.setIdentificationNumber("123");
        adult.setDateOfBirth(LocalDate.now().minusYears(25));

        underAge = new Customer();
        underAge.setId(2);
        underAge.setNames("Kid");
        underAge.setSurnames("Young");
        underAge.setEmail("kid@young.com");
        underAge.setIdentificationType(IdentificationType.CC);
        underAge.setIdentificationNumber("321");
        underAge.setDateOfBirth(LocalDate.now().minusYears(15));
    }



    @Test
    @DisplayName("findCustomerById - Should return customer when exists")
    void findCustomerById_ShouldReturnCustomer_WhenExists() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(adult));

        Customer result = customerService.findCustomerById(1L);

        assertEquals("John", result.getNames());
        verify(persistencePort).findById(1L);
    }

    @Test
    @DisplayName("findCustomerById - Should throw exception when not found")
    void findCustomerById_ShouldThrowException_WhenNotFound() {
        when(persistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.findCustomerById(99L));
    }



    @Test
    @DisplayName("saveCustomer - Should save customer when adult")
    void saveCustomer_ShouldSave_WhenAdult() {

        when(persistencePort.save(adult)).thenReturn(adult);

        Customer saved = customerService.saveCustomer(adult);

        assertNotNull(saved);
        verify(persistencePort).save(adult);
    }

    @Test
    @DisplayName("saveCustomer - Should throw exception when under age")
    void saveCustomer_ShouldThrowException_WhenUnderAge() {
        assertThrows(CustomerUnderAgeException.class,
                () -> customerService.saveCustomer(underAge));
    }



    @Test
    @DisplayName("updateCustomer - Should update customer when changes exist")
    void updateCustomer_ShouldUpdate_WhenChangesExist() {

        Customer existing = new Customer();
        existing.setId(1);
        existing.setNames("Old");
        existing.setSurnames("Name");
        existing.setDateOfBirth(LocalDate.now().minusYears(30));

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        when(persistencePort.save(any())).thenReturn(existing);

        Customer updated = customerService.updateCustomer(1L, adult);

        verify(persistencePort).save(existing);
        assertEquals("John", existing.getNames());
    }

    @Test
    @DisplayName("updateCustomer - Should return without saving when no changes")
    void updateCustomer_ShouldReturnWithoutSaving_WhenNoChanges() {

        Customer existing = new Customer();
        existing.setId(1);
        existing.setNames("John");
        existing.setSurnames("Doe");
        existing.setEmail("john@doe.com");
        existing.setIdentificationType(IdentificationType.CC);
        existing.setIdentificationNumber("123");
        existing.setDateOfBirth(LocalDate.now().minusYears(25));

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));

        Customer result = customerService.updateCustomer(1L, existing);

        verify(persistencePort, never()).save(any());
        assertEquals(result.getNames(), existing.getNames());
    }


    @Test
    @DisplayName("deleteCustomer - Should delete customer when no accounts")
    void deleteCustomer_ShouldDelete_WhenCustomerHasNoAccounts() {

        when(persistencePort.findById(1L)).thenReturn(Optional.of(adult));
        when(accountPersistencePort.findByCustomerId(1L)).thenReturn(Collections.emptyList());

        customerService.deleteCustomer(1L);

        verify(persistencePort).deleteById(1L);
    }

    @Test
    @DisplayName("deleteCustomer - Should throw exception when has accounts")
    void deleteCustomer_ShouldThrowException_WhenHasAccounts() {

        when(persistencePort.findById(1L)).thenReturn(Optional.of(adult));
        when(accountPersistencePort.findByCustomerId(1L))
                .thenReturn(List.of(new Account()));

        assertThrows(CustomerInvalidDeleteException.class,
                () -> customerService.deleteCustomer(1L));
    }


    @Test
    @DisplayName("partialUpdateCustomer - Should apply patch when changes exist")
    void partialUpdateCustomer_ShouldApplyPatch_WhenChangesExist() {

        Customer existing = new Customer();
        existing.setId(1);
        existing.setNames("John");
        existing.setEmail("old@mail.com");
        existing.setDateOfBirth(LocalDate.now().minusYears(30));

        CustomerPatchRequest patch = new CustomerPatchRequest();
        patch.setEmail("new@mail.com");

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        doAnswer(inv -> {
            existing.setEmail("new@mail.com");
            return null;
        }).when(restMapper).updateCustomerFromPatch(any(), any());

        when(persistencePort.save(existing)).thenReturn(existing);

        Customer result = customerService.partialUpdateCustomer(1L, patch);

        verify(persistencePort).save(existing);
        assertEquals("new@mail.com", result.getEmail());
    }

    @Test
    @DisplayName("partialUpdateCustomer - Should not save when no changes")
    void partialUpdateCustomer_ShouldNotSave_WhenNoChanges() {

        Customer existing = new Customer();
        existing.setId(1);
        existing.setNames("John");

        CustomerPatchRequest patch = new CustomerPatchRequest();

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));

        Customer result = customerService.partialUpdateCustomer(1L, patch);

        verify(persistencePort, never()).save(any());
        assertEquals(existing.getNames(), result.getNames());
    }

}
