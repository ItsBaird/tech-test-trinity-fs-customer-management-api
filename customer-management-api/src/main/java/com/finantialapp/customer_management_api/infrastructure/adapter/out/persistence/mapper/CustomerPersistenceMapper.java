package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper;

import com.finantialapp.customer_management_api.domain.model.Customer;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {


    CustomerEntity toCustomerEntity(Customer customer);

    Customer tocustomer(CustomerEntity entity);

    List<Customer> toCustomerList(List<CustomerEntity> entittyList);
}
