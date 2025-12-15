package com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.mapper;

import com.finantialapp.customer_management_api.domain.model.Customer;
import com.finantialapp.customer_management_api.infrastructure.adapter.out.persistence.entity.CustomerEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-14T18:18:32-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class CustomerPersistenceMapperImpl implements CustomerPersistenceMapper {

    @Override
    public CustomerEntity toCustomerEntity(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setId( customer.getId() );
        customerEntity.setIdentificationType( customer.getIdentificationType() );
        customerEntity.setIdentificationNumber( customer.getIdentificationNumber() );
        customerEntity.setNames( customer.getNames() );
        customerEntity.setSurnames( customer.getSurnames() );
        customerEntity.setEmail( customer.getEmail() );
        customerEntity.setDateOfBirth( customer.getDateOfBirth() );
        customerEntity.setCreatedAt( customer.getCreatedAt() );
        customerEntity.setModifiedAt( customer.getModifiedAt() );

        return customerEntity;
    }

    @Override
    public Customer tocustomer(CustomerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setId( entity.getId() );
        customer.setIdentificationType( entity.getIdentificationType() );
        customer.setIdentificationNumber( entity.getIdentificationNumber() );
        customer.setNames( entity.getNames() );
        customer.setSurnames( entity.getSurnames() );
        customer.setEmail( entity.getEmail() );
        customer.setDateOfBirth( entity.getDateOfBirth() );
        customer.setCreatedAt( entity.getCreatedAt() );
        customer.setModifiedAt( entity.getModifiedAt() );

        return customer;
    }

    @Override
    public List<Customer> toCustomerList(List<CustomerEntity> entittyList) {
        if ( entittyList == null ) {
            return null;
        }

        List<Customer> list = new ArrayList<Customer>( entittyList.size() );
        for ( CustomerEntity customerEntity : entittyList ) {
            list.add( tocustomer( customerEntity ) );
        }

        return list;
    }
}
