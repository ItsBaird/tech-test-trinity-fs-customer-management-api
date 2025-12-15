package com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper;

import com.finantialapp.customer_management_api.domain.model.Customer;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.CustomerPatchRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.CustomerResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-14T18:18:31-0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 22 (Oracle Corporation)"
)
@Component
public class CustomerRestMapperImpl implements CustomerRestMapper {

    @Override
    public Customer toCustomer(CustomerCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setIdentificationType( request.getIdentificationType() );
        customer.setIdentificationNumber( request.getIdentificationNumber() );
        customer.setNames( request.getNames() );
        customer.setSurnames( request.getSurnames() );
        customer.setEmail( request.getEmail() );
        customer.setDateOfBirth( request.getDateOfBirth() );

        return customer;
    }

    @Override
    public CustomerResponse toCustomerResponse(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerResponse.CustomerResponseBuilder customerResponse = CustomerResponse.builder();

        customerResponse.id( customer.getId() );
        customerResponse.identificationType( customer.getIdentificationType() );
        if ( customer.getIdentificationNumber() != null ) {
            customerResponse.identificationNumber( Long.parseLong( customer.getIdentificationNumber() ) );
        }
        customerResponse.names( customer.getNames() );
        customerResponse.surnames( customer.getSurnames() );
        customerResponse.email( customer.getEmail() );
        customerResponse.dateOfBirth( customer.getDateOfBirth() );
        customerResponse.createdAt( customer.getCreatedAt() );
        customerResponse.modifiedAt( customer.getModifiedAt() );

        return customerResponse.build();
    }

    @Override
    public List<CustomerResponse> toCustomerResponseList(List<Customer> customerList) {
        if ( customerList == null ) {
            return null;
        }

        List<CustomerResponse> list = new ArrayList<CustomerResponse>( customerList.size() );
        for ( Customer customer : customerList ) {
            list.add( toCustomerResponse( customer ) );
        }

        return list;
    }

    @Override
    public void updateCustomerFromPatch(CustomerPatchRequest patch, Customer customer) {
        if ( patch == null ) {
            return;
        }

        if ( patch.getIdentificationType() != null ) {
            customer.setIdentificationType( patch.getIdentificationType() );
        }
        if ( patch.getIdentificationNumber() != null ) {
            customer.setIdentificationNumber( patch.getIdentificationNumber() );
        }
        if ( patch.getNames() != null ) {
            customer.setNames( patch.getNames() );
        }
        if ( patch.getSurnames() != null ) {
            customer.setSurnames( patch.getSurnames() );
        }
        if ( patch.getEmail() != null ) {
            customer.setEmail( patch.getEmail() );
        }
        if ( patch.getDateOfBirth() != null ) {
            customer.setDateOfBirth( patch.getDateOfBirth() );
        }
    }
}
