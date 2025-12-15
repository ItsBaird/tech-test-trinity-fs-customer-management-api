package com.finantialapp.customer_management_api.application.service;

import com.finantialapp.customer_management_api.application.port.in.CustomerServicePort;
import com.finantialapp.customer_management_api.application.port.out.AccountPersistencePort;
import com.finantialapp.customer_management_api.application.port.out.CustomerPersistencePort;
import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerAlreadyExistsException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerInvalidDeleteException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerNotFoundException;
import com.finantialapp.customer_management_api.domain.exception.customer.CustomerUnderAgeException;
import com.finantialapp.customer_management_api.domain.model.Account;
import com.finantialapp.customer_management_api.domain.model.Customer;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.CustomerRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerPatchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerServicePort {

    private final CustomerPersistencePort persistencePort;
    private final CustomerRestMapper restMapper;
    private final AccountPersistencePort accountPersistencePort;

    @Override
    public Customer findCustomerById(Long id) {
        return persistencePort.findById(id).orElseThrow(CustomerNotFoundException::new);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return persistencePort.findAll();
    }

    @Override
    public Customer saveCustomer(Customer customer) {


        validateUniqueIdentification(
                customer.getIdentificationType(),
                customer.getIdentificationNumber(),
                null
        );

        LocalDate today = LocalDate.now();
        Period age = Period.between(customer.getDateOfBirth(), today);

        if (age.getYears() < 18) {
            throw new CustomerUnderAgeException();
        }

        customer.setCreatedAt(ZonedDateTime.now());
        return persistencePort.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {

        validateUniqueIdentification(
                customer.getIdentificationType(),
                customer.getIdentificationNumber(),
                id
        );

        // Validar edad mínima
        LocalDate today = LocalDate.now();
        Period age = Period.between(customer.getDateOfBirth(), today);

        if (age.getYears() < 18) {
            throw new CustomerUnderAgeException();
        }

        return persistencePort.findById(id)
                .map(savedCustomer -> {

                    //Verificar si NO hubo cambios ---
                    boolean noChanges =
                            Objects.equals(savedCustomer.getNames(), customer.getNames()) &&
                                    Objects.equals(savedCustomer.getSurnames(), customer.getSurnames()) &&
                                    Objects.equals(savedCustomer.getEmail(), customer.getEmail()) &&
                                    Objects.equals(savedCustomer.getIdentificationType(), customer.getIdentificationType()) &&
                                    Objects.equals(savedCustomer.getIdentificationNumber(), customer.getIdentificationNumber()) &&
                                    Objects.equals(savedCustomer.getDateOfBirth(), customer.getDateOfBirth());

                    if (noChanges) {
                        return savedCustomer;
                    }

                    //Aplicar cambios porque sí hubo modificaciones
                    savedCustomer.setNames(customer.getNames());
                    savedCustomer.setSurnames(customer.getSurnames());
                    savedCustomer.setEmail(customer.getEmail());
                    savedCustomer.setIdentificationType(customer.getIdentificationType());
                    savedCustomer.setIdentificationNumber(customer.getIdentificationNumber());
                    savedCustomer.setDateOfBirth(customer.getDateOfBirth());

                    savedCustomer.setModifiedAt(ZonedDateTime.now());

                    return persistencePort.save(savedCustomer);
                })
                .orElseThrow(CustomerNotFoundException::new);
    }


    @Override
    public void deleteCustomer(Long id) {

        //Validar si existe
        Customer customer = persistencePort.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

        //Validar si tiene cuentas asociadas
        List<Account> accounts = accountPersistencePort.findByCustomerId(id);

        if (!accounts.isEmpty()) {
            throw new CustomerInvalidDeleteException();
        }


        persistencePort.deleteById(id);
    }


    @Override
    public Customer partialUpdateCustomer(Long id, CustomerPatchRequest patch) {


        //Obtener el registro existente
        Customer savedCustomer = persistencePort.findById(id)
                .orElseThrow(CustomerNotFoundException::new);

        //Clonar antes de aplicar modificaciones
        Customer originalCopy = cloneCustomer(savedCustomer);

        //Aplicar los cambios del PATCH ignorando nulls (MapStruct)
        restMapper.updateCustomerFromPatch(patch, savedCustomer);

        //Validar edad si viene dateOfBirth en el PATCH
        if (patch.getDateOfBirth() != null) {
            LocalDate today = LocalDate.now();
            Period age = Period.between(savedCustomer.getDateOfBirth(), today);
            if (age.getYears() < 18) {
                throw new CustomerUnderAgeException();
            }
        }

        boolean identificationChanged =
                !Objects.equals(originalCopy.getIdentificationType(), savedCustomer.getIdentificationType()) ||
                        !Objects.equals(originalCopy.getIdentificationNumber(), savedCustomer.getIdentificationNumber());

        if (identificationChanged) {
            validateUniqueIdentification(
                    savedCustomer.getIdentificationType(),
                    savedCustomer.getIdentificationNumber(),
                    savedCustomer.getId()
            );
        }

        //Detectar si hubo o no cambios
        boolean noChanges =
                Objects.equals(originalCopy.getNames(), savedCustomer.getNames()) &&
                        Objects.equals(originalCopy.getSurnames(), savedCustomer.getSurnames()) &&
                        Objects.equals(originalCopy.getEmail(), savedCustomer.getEmail()) &&
                        Objects.equals(originalCopy.getIdentificationType(), savedCustomer.getIdentificationType()) &&
                        Objects.equals(originalCopy.getIdentificationNumber(), savedCustomer.getIdentificationNumber()) &&
                        Objects.equals(originalCopy.getDateOfBirth(), savedCustomer.getDateOfBirth());

        //Si no hubo cambios - regresar sin tocar modifiedAt ni guardar
        if (noChanges) {
            return savedCustomer;
        }

        //Si hubo cambios - actualizar modifiedAt
        savedCustomer.setModifiedAt(ZonedDateTime.now());

        return persistencePort.save(savedCustomer);
    }

    private Customer cloneCustomer(Customer c) {
        Customer copy = new Customer();
        copy.setIdentificationType(c.getIdentificationType());
        copy.setIdentificationNumber(c.getIdentificationNumber());
        copy.setNames(c.getNames());
        copy.setSurnames(c.getSurnames());
        copy.setEmail(c.getEmail());
        copy.setDateOfBirth(c.getDateOfBirth());
        copy.setCreatedAt(c.getCreatedAt());
        copy.setModifiedAt(c.getModifiedAt());
        return copy;
    }

    private void validateUniqueIdentification(
            IdentificationType type,
            String number,
            Long currentCustomerId
    ) {
        persistencePort.findByIdentification(type, number)
                .filter(existing ->
                        currentCustomerId == null || existing.getId() != currentCustomerId
                )
                .ifPresent(existing -> {
                    throw new CustomerAlreadyExistsException();
                });
    }





}
