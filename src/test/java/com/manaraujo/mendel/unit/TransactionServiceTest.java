package com.manaraujo.mendel.unit;

import com.manaraujo.mendel.model.Transaction;
import com.manaraujo.mendel.repository.TransactionRepository;
import com.manaraujo.mendel.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static com.manaraujo.mendel.model.Transaction.buildTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {

    @TestConfiguration
    static class testContextConfiguration {

        @Bean
        public TransactionService transactionService(TransactionRepository inMemoryTransactionRepository) {
            return new TransactionService(inMemoryTransactionRepository);
        }

    }

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository inMemoryTransactionRepository;

    @Test
    public void saveTransaction() {
        Transaction transaction = buildTransaction(1L, 100.0, "test", null);

        doNothing().when(inMemoryTransactionRepository).save(eq(transaction));

        transactionService.saveTransaction(transaction);

        verify(inMemoryTransactionRepository).save(eq(transaction));
    }

    @Test
    public void getTransactionIdsByType() {
        String type = "test";
        Set<Long> expected = Set.of(1L, 2L);

        when(inMemoryTransactionRepository.getIdsByType(eq(type))).thenReturn(expected);

        Set<Long> result = transactionService.getTransactionIdsByType(type);

        verify(inMemoryTransactionRepository).getIdsByType(eq(type));

        assertEquals(expected, result);
    }

    @Test
    public void sumTransactionAmountsTransitively_withoutChildren() {
        Long transactionId = 1L;
        Double expected = 100.0;

        Transaction transaction = buildTransaction(transactionId, 100.0, "test", null);

        when(inMemoryTransactionRepository.getById(eq(transactionId))).thenReturn(transaction);
        when(inMemoryTransactionRepository.getChildren(eq(transactionId))).thenReturn(Set.of());

        Double result = transactionService.sumTransactionAmountsTransitively(transactionId);

        verify(inMemoryTransactionRepository).getById(eq(transactionId));
        verify(inMemoryTransactionRepository).getChildren(eq(transactionId));

        assertEquals(expected, result);
    }

    @Test
    public void sumTransactionAmountsTransitively_withChildren() {
        Long transactionId = 1L;
        Double expected = 360.0;

        Transaction transaction = buildTransaction(transactionId, 100.0, "test", null);
        Transaction child1 = buildTransaction(transactionId, 120.0, "test", transactionId);
        Transaction child2 = buildTransaction(transactionId, 140.0, "test", transactionId);
        Set<Transaction> children = Set.of(child1, child2);

        when(inMemoryTransactionRepository.getById(eq(transactionId))).thenReturn(transaction);
        when(inMemoryTransactionRepository.getChildren(eq(transactionId))).thenReturn(children);

        Double result = transactionService.sumTransactionAmountsTransitively(transactionId);

        verify(inMemoryTransactionRepository).getById(eq(transactionId));
        verify(inMemoryTransactionRepository).getChildren(eq(transactionId));

        assertEquals(expected, result);
    }

}
