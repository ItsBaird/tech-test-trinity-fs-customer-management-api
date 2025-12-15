package com.finantialapp.customer_management_api.application.port.out;

import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import com.finantialapp.customer_management_api.domain.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface CustomerPersistencePort {

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    Customer save(Customer customer);

    void deleteById(Long id);

    Optional<Customer> findByIdentification(IdentificationType identificationType, String identificationNumber);


}
