package com.finantialapp.customer_management_api.infrastructure;

import com.finantialapp.customer_management_api.domain.enums.TransactionType;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.controller.TransactionRestAdapter;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.request.TransactionCreateRequest;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.model.response.TransactionResponse;
import com.finantialapp.customer_management_api.infrastructure.adapter.in.rest.service.TransactionQueryService;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class TransactionRestAdapterTest {

    @Mock
    private TransactionQueryService transactionQueryService;

    @InjectMocks
    private TransactionRestAdapter transactionRestAdapter;

    // ─── Tests ───────────────────────────────────────────────

    @Test
    @DisplayName("getAll - Should return list of enriched transactions")
    void testFindAllTransactions() {
        TransactionResponse resp = buildResponse();

        Mockito.when(transactionQueryService.findAllEnriched())
                .thenReturn(List.of(resp));

        List<TransactionResponse> result = transactionRestAdapter.findAllTransactions();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(TransactionType.CONSIGNACION, result.get(0).getTransactionType());
        assertEquals("4600000001", result.get(0).getDestinationAccountNumber());
        assertNull(result.get(0).getSourceAccountNumber());
    }

    @Test
    @DisplayName("getById - Should return enriched transaction by id")
    void testFindById() {
        TransactionResponse resp = buildResponse();

        Mockito.when(transactionQueryService.findEnrichedById(1L))
                .thenReturn(resp);

        TransactionResponse result = transactionRestAdapter.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals(TransactionType.CONSIGNACION, result.getTransactionType());
        assertEquals("4600000001", result.getDestinationAccountNumber());
        assertNull(result.getSourceAccountNumber());
    }

    @Test
    @DisplayName("create - Should save and return enriched transaction")
    void testCreateTransaction() {
        TransactionCreateRequest req = TransactionCreateRequest.builder()
                .transactionType(TransactionType.CONSIGNACION)
                .amount(new BigDecimal("500"))
                .sourceAccountId(null)
                .destinationAccountId(10L)
                .build();

        TransactionResponse resp = buildResponse();

        Mockito.when(transactionQueryService.saveAndEnrich(req))
                .thenReturn(resp);

        TransactionResponse result = transactionRestAdapter.createTransaction(req);

        assertEquals(1L, result.getId());
        assertEquals(TransactionType.CONSIGNACION, result.getTransactionType());
        assertEquals(new BigDecimal("500"), result.getAmount());
        assertEquals("4600000001", result.getDestinationAccountNumber());
        assertNull(result.getSourceAccountNumber());
    }

    // ─── Helpers ─────────────────────────────────────────────

    private TransactionResponse buildResponse() {
        return TransactionResponse.builder()
                .id(1L)
                .transactionType(TransactionType.CONSIGNACION)
                .amount(new BigDecimal("500"))
                .sourceAccountNumber(null)
                .destinationAccountNumber("4600000001")
                .transactionDate(LocalDateTime.now())
                .build();
    }
}