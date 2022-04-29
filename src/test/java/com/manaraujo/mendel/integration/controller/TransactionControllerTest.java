package com.manaraujo.mendel.integration.controller;

import com.manaraujo.mendel.AbstractTest;
import com.manaraujo.mendel.model.Transaction;
import com.manaraujo.mendel.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Map;
import java.util.Set;

import static com.manaraujo.mendel.model.Transaction.buildTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest extends AbstractTest {

    @SpyBean
    private TransactionRepository inMemoryTransactionRepository;

    @Test
    public void saveTransaction() throws Exception {
        String uri = "/api/v1/transactions/1";

        Map<String, String> body = Map.of(
                "type", "test",
                "amount", "100.0"
        );

        Transaction expectedSaved = buildTransaction(1L, 100.0, "test", null);

        String mvcResult = mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(body)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, String> result = toObject(mvcResult, Map.class);

        Transaction transaction = inMemoryTransactionRepository.getById(1L);

        assertEquals("ok", result.get("status"));
        assertEquals(expectedSaved, transaction);
    }

    @Test
    public void getTransactionIdsByType() throws Exception {
        String uri = "/api/v1/transactions/types/test1";

        Transaction transaction1 = buildTransaction(1L, 100.0, "test1", null);
        Transaction transaction2 = buildTransaction(2L, 200.0, "test1", null);
        Transaction transaction3 = buildTransaction(3L, 300.0, "test2", null);

        inMemoryTransactionRepository.save(transaction1);
        inMemoryTransactionRepository.save(transaction2);
        inMemoryTransactionRepository.save(transaction3);

        String mvcResult = mvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Set<Long> result = mapFromJsonSet(mvcResult, Long.class);

        assertThat(result).hasSize(2);
        assertEquals(result, Set.of(1L, 2L));
    }

    @Test
    public void sumTransactionAmountsWithoutChildren() throws Exception {
        String uri = "/api/v1/transactions/sum/1";

        Transaction transaction1 = buildTransaction(1L, 100.0, "test1", null);
        Transaction transaction2 = buildTransaction(2L, 200.0, "test1", null);

        inMemoryTransactionRepository.save(transaction1);
        inMemoryTransactionRepository.save(transaction2);

        String mvcResult = mvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, Double> result = toObject(mvcResult, Map.class);

        assertEquals(100.0, result.get("sum"));
    }

    @Test
    public void sumTransactionAmountsWithChildren() throws Exception {
        String uri = "/api/v1/transactions/sum/1";

        Transaction transaction1 = buildTransaction(1L, 100.0, "test1", null);
        Transaction transaction2 = buildTransaction(2L, 200.0, "test1", 1L);

        inMemoryTransactionRepository.save(transaction1);
        inMemoryTransactionRepository.save(transaction2);

        String mvcResult = mvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, Double> result = toObject(mvcResult, Map.class);

        assertEquals(300.0, result.get("sum"));
    }

}
