package com.manaraujo.mendel.unit;

import com.manaraujo.mendel.AbstractTest;
import com.manaraujo.mendel.exception.NotFoundException;
import com.manaraujo.mendel.model.Transaction;
import com.manaraujo.mendel.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Map;

import static com.manaraujo.mendel.model.Transaction.buildTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest extends AbstractTest {

    @MockBean
    private TransactionService transactionService;

    @Test
    public void saveTransaction() throws Exception {
        String uri = "/api/v1/transactions/1";

        Map<String, String> body = Map.of(
                "type", "test",
                "amount", "100.0",
                "parent_id", "2"
        );

        Transaction transaction = buildTransaction(1L, 100.0, "test", 2L);

        doNothing().when(transactionService).saveTransaction(eq(transaction));

        String mvcResult = mvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(body)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(transactionService).saveTransaction(eq(transaction));

        Map<String, String> result = toObject(mvcResult, Map.class);

        assertEquals("ok", result.get("status"));
    }

    @Test
    public void getTransactionIdsByType() throws Exception {
        String uri = "/api/v1/transactions/types/test";

        List<Long> expected = List.of(1L, 2L);

        when(transactionService.getTransactionIdsByType(eq("test"))).thenReturn(expected);

        String mvcResult = mvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(transactionService).getTransactionIdsByType(eq("test"));

        List<Long> result = mapFromJsonList(mvcResult, Long.class);

        assertThat(result).hasSize(2);
        assertEquals(1L, result.get(0));
        assertEquals(2L, result.get(1));
    }

    @Test
    public void getTransactionIdsByType_emptyResult() throws Exception {
        String uri = "/api/v1/transactions/types/test";

        List<Long> expected = List.of();

        when(transactionService.getTransactionIdsByType(eq("test"))).thenReturn(expected);

        String mvcResult = mvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(transactionService).getTransactionIdsByType(eq("test"));

        List<Long> result = mapFromJsonList(mvcResult, Long.class);

        assertThat(result).isEmpty();
    }

    @Test
    public void sumTransactionAmountsTransitively() throws Exception {
        String uri = "/api/v1/transactions/sum/1";

        Double expected = 100.0;

        when(transactionService.sumTransactionAmountsTransitively(eq(1L))).thenReturn(expected);

        String mvcResult = mvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(transactionService).sumTransactionAmountsTransitively(eq(1L));

        Map<String, Double> result = toObject(mvcResult, Map.class);

        assertEquals(100.0, result.get("sum"));
    }

    @Test
    public void sumTransactionAmountsTransitively_TransactionIdNotFound() throws Exception {
        String uri = "/api/v1/transactions/sum/1";

        when(transactionService.sumTransactionAmountsTransitively(eq(1L))).thenThrow(new NotFoundException());

        mvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(transactionService).sumTransactionAmountsTransitively(eq(1L));
    }

}
