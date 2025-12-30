package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository;

import com.finantialapp.customer_management_api.domain.enums.IdentificationType;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByIdentificationTypeAndIdentificationNumber(
            IdentificationType identificationType,
            String identificationNumber);

    Optional<CustomerEntity> findByEmail(String email);
}
