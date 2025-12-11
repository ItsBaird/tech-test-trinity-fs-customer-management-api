package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository;

import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByAccountNumber(Long accountNumber);

    List<AccountEntity> findByCustomerId(Long customerId);
}
