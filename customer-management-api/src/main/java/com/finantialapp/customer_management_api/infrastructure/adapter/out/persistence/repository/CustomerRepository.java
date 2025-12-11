package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository;

import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
