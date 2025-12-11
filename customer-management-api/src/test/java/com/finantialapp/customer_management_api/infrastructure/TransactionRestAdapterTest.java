package com.finantialapp.customer_management_api.infrastructure;


import com.finantialapp.customer_management_api.application.port.in.TransactionServicePort;
import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import com.finantialapp.customer_management_api.domain.model.Transaction;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.controller.TransactionRestAdapter;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.mapper.TransactionRestMapper;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.TransactionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TransactionRestAdapterTest {

    @Mock
    private TransactionRestMapper transactionRestMapper;

    @Mock
    private TransactionServicePort transactionServicePort;

    @InjectMocks
    TransactionRestAdapter transactionRestAdapter;



    @Test
    @DisplayName("getAll - Test for findAllTransactions method")
    void testFindAllTransactions() {
        Transaction tx = buildTransaction();
        TransactionResponse resp = buildResponse();

        Mockito.when(transactionServicePort.findAllTransactions()).thenReturn(List.of(tx));
        Mockito.when(transactionRestMapper.toTransactionResponseList(List.of(tx)))
                .thenReturn(List.of(resp));

        List<TransactionResponse> result = transactionRestAdapter.findAllTransactions();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }



    @Test
    @DisplayName("getById - Test for findById method")
    void testFindById() {
        Transaction tx = buildTransaction();
        TransactionResponse resp = buildResponse();

        Mockito.when(transactionServicePort.findTransactionById(1L)).thenReturn(tx);
        Mockito.when(transactionRestMapper.toTransactionResponse(tx)).thenReturn(resp);

        TransactionResponse result = transactionRestAdapter.findById(1L);

        assertEquals(1L, result.getId());
    }




    @Test
    @DisplayName("create - Test for createTransaction method")
    void testCreateTransaction() {
        TransactionCreateRequest req = TransactionCreateRequest.builder()
                .transactionType(TransactionType.CONSIGNACION)  // CORREGIDO
                .amount(new BigDecimal("500"))
                .sourceAccountId(null)
                .destinationAccountId(10L)
                .build();

        Transaction tx = buildTransaction();
        TransactionResponse resp = buildResponse();

        Mockito.when(transactionRestMapper.toTransaction(req)).thenReturn(tx);
        Mockito.when(transactionServicePort.saveTransaction(tx)).thenReturn(tx);
        Mockito.when(transactionRestMapper.toTransactionResponse(tx)).thenReturn(resp);

        TransactionResponse result = transactionRestAdapter.createTransaction(req);

        assertEquals(1L, result.getId());
        assertEquals(TransactionType.CONSIGNACION, result.getTransactionType()); // CORREGIDO
    }


    // Helpers
    // -----------------------------------------
    private Transaction buildTransaction() {
        return new Transaction(
                1L,
                TransactionType.CONSIGNACION,       // CORREGIDO
                new BigDecimal("500"),
                null,
                10L,
                LocalDateTime.now()
        );
    }

    private TransactionResponse buildResponse() {
        return TransactionResponse.builder()
                .id(1L)
                .transactionType(TransactionType.CONSIGNACION)  // CORREGIDO
                .amount(new BigDecimal("500"))
                .sourceAccountId(null)
                .destinationAccountId(10L)
                .transactionDate(LocalDateTime.now())
                .build();
    }
}
