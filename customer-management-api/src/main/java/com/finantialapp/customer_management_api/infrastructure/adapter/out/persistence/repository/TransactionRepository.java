package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.repository;

import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {


}
