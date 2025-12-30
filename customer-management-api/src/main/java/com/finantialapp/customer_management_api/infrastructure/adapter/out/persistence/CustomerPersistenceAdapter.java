package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence;

import com.finantialapp.customer_management_api.application.port.out.CustomerPersistencePort;
import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import com.finantialapp.customer_management_api.domain.model.Customer;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper.CustomerPersistenceMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerPersistenceAdapter implements CustomerPersistencePort {


    private final CustomerRepository customerRepository;
    private final CustomerPersistenceMapper mapper;

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id)
                .map(mapper::tocustomer);
    }

    @Override
    public List<Customer> findAll() {
        return mapper.toCustomerList(customerRepository.findAll());
    }

    @Override
    public Customer save(Customer customer) {

        CustomerEntity entity;

        if (customer.getId() != 0) {
            // Es UPDATE — obtener la entidad existente
            entity = customerRepository.findById(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // solo actualizar campos simples
            entity.setNames(customer.getNames());
            entity.setSurnames(customer.getSurnames());
            entity.setEmail(customer.getEmail());
            entity.setIdentificationType(customer.getIdentificationType());
            entity.setIdentificationNumber(customer.getIdentificationNumber());
            entity.setDateOfBirth(customer.getDateOfBirth());
            entity.setModifiedAt(customer.getModifiedAt());

        } else {
            // Es CREATE — se mapea normal
            entity = mapper.toCustomerEntity(customer);
        }

        return mapper.tocustomer(customerRepository.save(entity));
    }


    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Optional<Customer> findByIdentification(IdentificationType identificationType, String identificationNumber) {
        return customerRepository
                .findByIdentificationTypeAndIdentificationNumber(
                        identificationType,
                        identificationNumber
                )
                .map(mapper::tocustomer);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(mapper::tocustomer);
    }
}
